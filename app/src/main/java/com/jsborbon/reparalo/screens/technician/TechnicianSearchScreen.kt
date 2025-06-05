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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.Category
import com.jsborbon.reparalo.models.User
import com.jsborbon.reparalo.navigation.components.NavigationBottomBar
import com.jsborbon.reparalo.screens.technician.components.TechnicianCard
import com.jsborbon.reparalo.viewmodels.CategoryViewModel
import com.jsborbon.reparalo.viewmodels.TechnicianListViewModel

@Composable
fun TechnicianSearchScreen(
    navController: NavHostController,
) {
    val viewModel: TechnicianListViewModel = viewModel()
    val categoryViewModel: CategoryViewModel = viewModel()

    val techniciansState by viewModel.technicians.collectAsState()
    val query = remember { mutableStateOf("") }
    val categoriesState by categoryViewModel.categories.collectAsState()

    val allSpecialties: List<Category> = when (categoriesState) {
        is ApiResponse.Success -> {
            listOf(Category(id = "all", name = "Todos")) + (categoriesState as ApiResponse.Success<List<Category>>).data
        }
        else -> listOf(Category(id = "all", name = "Todos"))
    }

    val selectedCategory = remember { mutableStateOf(allSpecialties.first()) }

    LaunchedEffect(Unit) {
        viewModel.loadAllTechnicians()
    }

    Scaffold(
        bottomBar = {
            NavigationBottomBar(selectedIndex = 2, navController = navController, onTabChange = {})
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
                items(allSpecialties, key = { it.id }) { category ->
                    val isSelected = selectedCategory.value.name == category.name
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedCategory.value = category
                            if (category.name == "Todos") {
                                viewModel.loadAllTechnicians()
                            } else {
                                viewModel.loadTechniciansBySpecialty(category.name)
                            }
                        },
                        label = { Text(category.name) },
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

                is ApiResponse.Idle -> {
                    Text(
                        text = "Esperando técnicos...",
                        style = MaterialTheme.typography.bodyMedium,
                    )
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
