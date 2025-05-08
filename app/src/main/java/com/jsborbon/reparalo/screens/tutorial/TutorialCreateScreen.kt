package com.jsborbon.reparalo.screens.tutorial

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberSnackbarHostState
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
import com.jsborbon.reparalo.models.Tutorial
import com.jsborbon.reparalo.viewmodels.TutorialsViewModel
import java.util.UUID

@Composable
fun TutorialCreateScreen(
    navController: NavController,
    viewModel: TutorialsViewModel = viewModel()
) {
    val createState by viewModel.createState.collectAsState()
    val snackbarHostState = rememberSnackbarHostState()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var difficulty by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Crear Tutorial") })
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Categoría") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = difficulty,
                onValueChange = { difficulty = it },
                label = { Text("Nivel de dificultad") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = duration,
                onValueChange = { duration = it },
                label = { Text("Duración estimada") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = author,
                onValueChange = { author = it },
                label = { Text("Autor") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val tutorial = Tutorial(
                        id = UUID.randomUUID().toString(),
                        title = title,
                        description = description,
                        category = category,
                        difficultyLevel = difficulty,
                        estimatedDuration = duration,
                        author = author,
                        materials = emptyList(),
                        averageRating = 0f
                    )
                    viewModel.createTutorial(tutorial)
                },
                enabled = createState !is ApiResponse.Loading,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Crear")
            }

            when (createState) {
                is ApiResponse.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }

                is ApiResponse.Success -> {
                    LaunchedEffect(Unit) {
                        snackbarHostState.showSnackbar("Tutorial creado con éxito")
                        viewModel.resetCreateState()
                        navController.popBackStack()
                    }
                }

                is ApiResponse.Failure -> {
                    val message = (createState as ApiResponse.Failure).errorMessage
                    LaunchedEffect(message) {
                        snackbarHostState.showSnackbar("Error al crear tutorial: $message")
                        viewModel.resetCreateState()
                    }
                }

                null -> {}
            }
        }
    }
}
