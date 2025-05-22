package com.jsborbon.reparalo.screens.technician

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jsborbon.reparalo.components.NavigationBottomBar
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.User
import com.jsborbon.reparalo.viewmodels.TechnicianListViewModel

@Composable
fun TechnicianSearchScreen(
    navController: NavController,
    viewModel: TechnicianListViewModel = hiltViewModel(),
) {
    val techniciansState by viewModel.technicians.collectAsState()
    val selectedSpecialty by viewModel.selectedSpecialty.collectAsState()
    val query = remember { mutableStateOf("") }

    val specialties = listOf(
        "Electricidad",
        "Plomería",
        "Carpintería",
        "Electrónica",
        "Jardinería",
        "Automotriz",
        "Pintura",
        "Albañilería",
    )

    LaunchedEffect(Unit) {
        viewModel.loadAllTechnicians()
    }

    Scaffold(
        bottomBar = {
            NavigationBottomBar(selectedIndex = 2, navController = navController)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
        ) {
            Text(
                text = "Buscar Técnicos",
                style = MaterialTheme.typography.headlineSmall,
            )

            OutlinedTextField(
                value = query.value,
                onValueChange = { query.value = it },
                label = { Text("Buscar por nombre o correo") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 16.dp),
            ) {
                items(specialties) { specialty ->
                    val isSelected = specialty == selectedSpecialty
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            if (isSelected) {
                                viewModel.loadAllTechnicians()
                            } else {
                                viewModel.loadTechniciansBySpecialty(specialty)
                            }
                        },
                        label = { Text(specialty) },
                    )
                }
            }

            when (val state = techniciansState) {
                is ApiResponse.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is ApiResponse.Failure -> {
                    Text(
                        text = "Error al cargar técnicos: ${state.errorMessage}",
                        color = MaterialTheme.colorScheme.error,
                    )
                }

                is ApiResponse.Success -> {
                    val filtered = filterTechnicians(state.data, query.value)
                    if (filtered.isEmpty()) {
                        Text("No se encontraron técnicos.")
                    } else {
                        LazyColumn(contentPadding = PaddingValues(bottom = 100.dp)) {
                            items(filtered, key = { it.uid }) { technician ->
                                TechnicianCard(
                                    technician = technician,
                                    padding = PaddingValues(vertical = 8.dp),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun filterTechnicians(list: List<User>, query: String): List<User> {
    if (query.isBlank()) return list
    return list.filter {
        it.name.contains(query, ignoreCase = true) ||
            it.email.contains(query, ignoreCase = true)
    }
}
