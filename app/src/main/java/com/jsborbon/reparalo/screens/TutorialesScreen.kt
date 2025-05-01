package com.jsborbon.reparalo.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.Tutorial
import com.jsborbon.reparalo.navigation.Routes
import com.jsborbon.reparalo.ui.theme.RepairYellow
import com.jsborbon.reparalo.viewmodels.TutorialesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialesScreen(navController: NavController) {
    val viewModel: TutorialesViewModel = viewModel()
    val tutoriales by viewModel.tutoriales.collectAsState()
    val tutorialesPorCategoria by viewModel.tutorialesPorCategoria.collectAsState()
    val categoriaSeleccionada by viewModel.categoriaSeleccionada.collectAsState()

    val categoriasDisponibles = listOf(
        "Electricidad",
        "Plomería",
        "Carpintería",
        "Electrónica",
        "Jardinería",
        "Automotriz",
        "Pintura",
        "Albañilería",
    )

    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tutoriales") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Implementar búsqueda */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            // Barra de búsqueda
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Buscar por título, descripción o autor") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
            )

            // Filtro por categorías
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
            ) {
                items(categoriasDisponibles) { categoria ->
                    val isSelected = categoria == categoriaSeleccionada

                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            if (isSelected) {
                                viewModel.seleccionarCategoria(null)
                                viewModel.seleccionarCategoria(categoria)
                            }
                        },
                        label = { Text(categoria) },
                        modifier = Modifier.padding(end = 8.dp),
                    )
                }
            }

            // Lista de tutoriales
            when (val tutorialesData = if (categoriaSeleccionada != null) tutorialesPorCategoria else tutoriales) {
                is ApiResponse.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is ApiResponse.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Error al cargar tutoriales")
                            Text(tutorialesData.errorMessage, color = MaterialTheme.colorScheme.error)
                            Button(onClick = { viewModel.cargarTutoriales() }) {
                                Text("Reintentar")
                            }
                        }
                    }
                }
                is ApiResponse.Success -> {
                    val tutorialesFiltrados = tutorialesData.data.filter {
                        if (searchQuery.isBlank()) {
                            true
                        } else {
                            val query = searchQuery.trim().lowercase()
                            it.titulo.lowercase().contains(query) ||
                                it.descripcion.lowercase().contains(query) ||
                                it.autor.lowercase().contains(query)
                        }
                    }

                    if (tutorialesFiltrados.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No se encontraron tutoriales")
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            items(tutorialesFiltrados) { tutorial ->
                                tutorialCard(tutorial = tutorial, onClick = {
                                    navController.navigate("${Routes.TUTORIAL_DETAIL}/${tutorial.id}")
                                })
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun tutorialCard(
    tutorial: Tutorial,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = tutorial.titulo,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "Por: ${tutorial.autor}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = tutorial.categoria,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                )

                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "•", color = Color.Gray)
                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = tutorial.nivelDificultad,
                    style = MaterialTheme.typography.bodyMedium,
                    color = when (tutorial.nivelDificultad) {
                        "Fácil" -> Color.Green
                        "Intermedio" -> RepairYellow
                        "Avanzado" -> Color.Red
                        else -> Color.Gray
                    },
                )

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = RepairYellow,
                    modifier = Modifier.size(16.dp),
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = String.format("%.1f", tutorial.calificacionPromedio),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = tutorial.descripcion,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Duración: ${tutorial.duracionEstimada}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Ver tutorial",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}
