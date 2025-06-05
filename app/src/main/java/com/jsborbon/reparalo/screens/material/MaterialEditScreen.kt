package com.jsborbon.reparalo.screens.material

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.viewmodels.MaterialsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialEditScreen(
    navController: NavController,
    materialId: String,
) {
    val viewModel: MaterialsViewModel = viewModel()

    val materialState = viewModel.materialDetail.collectAsState()
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    LaunchedEffect(materialId) {
        viewModel.loadMaterialById(materialId)
    }

    val state = materialState.value
    val material = (state as? ApiResponse.Success)?.data

    LaunchedEffect(material) {
        material?.let {
            name = it.name
            description = it.description
            quantity = it.quantity.toString()
            price = it.price.toString()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar material") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
            )
        },
    ) { innerPadding ->
        when (state) {
            is ApiResponse.Idle -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "Cargando material...")
                }
            }

            is ApiResponse.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            is ApiResponse.Failure -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "Error: ${state.errorMessage}")
                }
            }

            is ApiResponse.Success -> {
                material?.let {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Nombre") },
                            modifier = Modifier.fillMaxWidth(),
                        )
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Descripción") },
                            modifier = Modifier.fillMaxWidth(),
                        )
                        OutlinedTextField(
                            value = quantity,
                            onValueChange = { quantity = it },
                            label = { Text("Cantidad") },
                            modifier = Modifier.fillMaxWidth(),
                        )
                        OutlinedTextField(
                            value = price,
                            onValueChange = { price = it },
                            label = { Text("Precio") },
                            modifier = Modifier.fillMaxWidth(),
                        )

                        Button(
                            onClick = {
                                val updated = it.copy(
                                    name = name,
                                    description = description,
                                    quantity = quantity.toIntOrNull() ?: 0,
                                    price = price.toFloatOrNull() ?: 0f,
                                )
                                viewModel.updateMaterial(updated)
                                navController.popBackStack()
                            },
                            modifier = Modifier.align(Alignment.End),
                        ) {
                            Icon(imageVector = Icons.Default.Done, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Guardar")
                        }
                    }
                }
            }
        }
    }
}
