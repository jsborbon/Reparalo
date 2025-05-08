package com.jsborbon.reparalo.screens.tutorial

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
import com.jsborbon.reparalo.navigation.Routes
import com.jsborbon.reparalo.viewmodels.TutorialsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialsScreen(navController: NavController) {
    val viewModel: TutorialsViewModel = viewModel()
    val allTutorials by viewModel.tutorials.collectAsState()
    val tutorialsByCategory by viewModel.tutorialsByCategory.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    val availableCategories = listOf(
        "Electricidad", "Plomería", "Carpintería", "Electrónica",
        "Jardinería", "Automotriz", "Pintura", "Albañilería"
    )

    var searchQuery by remember { mutableStateOf("") }
    var triggerSearch by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Tutoriales") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { triggerSearch = true }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Buscar")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text(text = "Buscar por título, descripción o autor") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                },
                singleLine = true
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(availableCategories) { category ->
                    val isSelected = category == selectedCategory
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            viewModel.selectCategory(
                                if (isSelected) null else category
                            )
                        },
                        label = { Text(text = category) },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }

            val tutorialsState = if (selectedCategory != null) tutorialsByCategory else allTutorials

            when (val state = tutorialsState) {
                is ApiResponse.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is ApiResponse.Failure -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Error al cargar tutoriales")
                            Text(
                                text = state.errorMessage,
                                color = MaterialTheme.colorScheme.error
                            )
                            Button(onClick = { viewModel.loadTutorials() }) {
                                Text(text = "Reintentar")
                            }
                        }
                    }
                }

                is ApiResponse.Success -> {
                    val query = searchQuery.trim().lowercase()
                    val filteredTutorials = if (triggerSearch) {
                        triggerSearch = false
                        state.data.filter {
                            query.isBlank() ||
                                it.title.lowercase().contains(query) ||
                                it.description.lowercase().contains(query) ||
                                it.author.lowercase().contains(query)
                        }
                    } else {
                        emptyList()
                    }

                    if (triggerSearch) {
                        // Nothing rendered yet (waiting to trigger)
                    } else if (filteredTutorials.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "No se encontraron tutoriales")
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(filteredTutorials, key = { it.id }) { tutorial ->
                                TutorialCard(
                                    tutorial = tutorial,
                                    onClick = {
                                        navController.navigate("${Routes.TUTORIAL_DETAIL}/${tutorial.id}")
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
