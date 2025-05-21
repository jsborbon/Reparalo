package com.jsborbon.reparalo.screens.tutorial

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.navigation.Routes
import com.jsborbon.reparalo.viewmodels.TutorialsViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialsScreen(navController: NavController) {
    val viewModel: TutorialsViewModel = viewModel()
    val allTutorials by viewModel.tutorials.collectAsState()
    val tutorialsByCategory by viewModel.tutorialsByCategory.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val deleteState by viewModel.deleteState.collectAsState()
    val context = LocalContext.current

    var searchQuery by remember { mutableStateOf("") }
    var triggerSearch by remember { mutableStateOf(false) }
    var tutorialToDeleteId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.uiMessage.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    val availableCategories = listOf(
        "Electricidad", "Plomería", "Carpintería", "Electrónica",
        "Jardinería", "Automotriz", "Pintura", "Albañilería",
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tutoriales") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { triggerSearch = true }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            if (deleteState is ApiResponse.Loading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Buscar por título, descripción o autor") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                singleLine = true,
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
            ) {
                items(availableCategories) { category ->
                    val isSelected = category == selectedCategory
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            viewModel.selectCategory(if (isSelected) null else category)
                        },
                        label = { Text(category) },
                        modifier = Modifier.padding(end = 8.dp),
                    )
                }
            }

            val tutorialsState = if (selectedCategory != null) tutorialsByCategory else allTutorials

            when (val state = tutorialsState) {
                is ApiResponse.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is ApiResponse.Failure -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Error al cargar tutoriales")
                            Text(
                                text = state.errorMessage,
                                color = MaterialTheme.colorScheme.error,
                            )
                            Button(onClick = { viewModel.loadTutorials() }) {
                                Text("Reintentar")
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
                        state.data
                    }

                    if (filteredTutorials.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text("No se encontraron tutoriales")
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            items(filteredTutorials, key = { it.id }) { tutorial ->
                                ElevatedCard(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = {
                                        navController.navigate("${Routes.TUTORIAL_DETAIL}/${tutorial.id}")
                                    }
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(tutorial.title, style = MaterialTheme.typography.titleMedium)
                                        Text(tutorial.description, style = MaterialTheme.typography.bodyMedium)
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text("Autor: ${tutorial.author}", style = MaterialTheme.typography.labelMedium)
                                            IconButton(onClick = {
                                                tutorialToDeleteId = tutorial.id
                                            }) {
                                                Icon(
                                                    imageVector = Icons.Default.Delete,
                                                    contentDescription = "Eliminar",
                                                    tint = MaterialTheme.colorScheme.error
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (tutorialToDeleteId != null) {
        AlertDialog(
            onDismissRequest = { tutorialToDeleteId = null },
            title = { Text("Eliminar tutorial") },
            text = { Text("¿Estás seguro de que deseas eliminar este tutorial? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteTutorial(tutorialToDeleteId!!)
                    tutorialToDeleteId = null
                }) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { tutorialToDeleteId = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
