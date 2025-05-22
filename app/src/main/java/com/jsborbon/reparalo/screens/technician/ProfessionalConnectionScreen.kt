package com.jsborbon.reparalo.screens.technician

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.viewmodels.TechnicianListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfessionalConnectionScreen(
    navController: NavController,
    viewModel: TechnicianListViewModel = viewModel(),
) {
    val technicianState = viewModel.technicians.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAllTechnicians()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Profesionales disponibles") })
        },
    ) { innerPadding ->
        when (val state = technicianState.value) {
            is ApiResponse.Loading -> {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }

            is ApiResponse.Success -> {
                val technicians = state.data
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(16.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(technicians, key = { it.uid }) { technician ->
                        TechnicianCard(
                            technician = technician,
                            onClick = {
                                navController.navigate("technician_profile/${technician.uid}")
                            },
                        )
                    }
                }
            }

            is ApiResponse.Failure -> {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(16.dp),
                ) {
                    Text(
                        text = "Error al cargar profesionales: ${state.errorMessage}",
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
        }
    }
}
