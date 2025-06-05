package com.jsborbon.reparalo.screens.tutorial

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import com.jsborbon.reparalo.models.Author
import com.jsborbon.reparalo.models.Material
import com.jsborbon.reparalo.models.Tutorial
import com.jsborbon.reparalo.viewmodels.TutorialsViewModel
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialEditScreen(
    navController: NavController,
    tutorialId: String,
) {
    val viewModel: TutorialsViewModel = viewModel()

    val updateState by viewModel.updateState.collectAsState()
    val tutorialsState by viewModel.tutorials.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var difficulty by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var author by remember { mutableStateOf(Author()) }
    var materials by remember { mutableStateOf<List<Material>>(emptyList()) }
    var newMaterial by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var videoUrl by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadTutorials()
    }

    LaunchedEffect(tutorialsState) {
        if (tutorialsState is ApiResponse.Success) {
            val tutorial = (tutorialsState as ApiResponse.Success<List<Tutorial>>).data
                .find { it.id == tutorialId }

            tutorial?.let {
                title = it.title
                description = it.description
                category = it.category
                difficulty = it.difficultyLevel
                duration = it.estimatedDuration
                author = it.author
                materials = it.materials
                videoUrl = it.videoUrl
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Editar Tutorial") })
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Categoría") },
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = difficulty,
                onValueChange = { difficulty = it },
                label = { Text("Nivel de dificultad") },
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = duration,
                onValueChange = { duration = it },
                label = { Text("Duración estimada") },
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = author.name,
                onValueChange = { author = author.copy(name = it) },
                label = { Text("Autor") },
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = imageUrl,
                onValueChange = { imageUrl = it },
                label = { Text("URL de imagen") },
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = videoUrl,
                onValueChange = { videoUrl = it },
                label = { Text("URL de video") },
                modifier = Modifier.fillMaxWidth(),
            )

            Text("Materiales", style = MaterialTheme.typography.titleMedium)

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                items(materials) { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(text = item.name, modifier = Modifier.weight(1f))
                        IconButton(onClick = {
                            materials = materials.filterNot { it.id == item.id }
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar material")
                        }
                    }
                }
            }

            OutlinedTextField(
                value = newMaterial,
                onValueChange = { newMaterial = it },
                label = { Text("Nuevo material") },
                modifier = Modifier.fillMaxWidth(),
            )

            Button(
                onClick = {
                    if (newMaterial.isNotBlank()) {
                        val newItem = Material(
                            id = System.currentTimeMillis().toString(),
                            name = newMaterial.trim(),
                            quantity = 1,
                            description = "",
                            price = 0f,
                        )
                        materials = materials + newItem
                        newMaterial = ""
                    }
                },
                modifier = Modifier.align(Alignment.End),
            ) {
                Text("Añadir material")
            }

            Button(
                onClick = {
                    val updatedTutorial = Tutorial(
                        id = tutorialId,
                        title = title,
                        description = description,
                        category = category,
                        difficultyLevel = difficulty,
                        estimatedDuration = duration,
                        author = author,
                        materials = materials,
                        videoUrl = videoUrl,
                        publicationDate = Date(),
                        averageRating = 0f,
                    )
                    viewModel.updateTutorial(tutorialId, updatedTutorial)
                },
                enabled = updateState !is ApiResponse.Loading,
                modifier = Modifier.align(Alignment.End),
            ) {
                Text("Guardar cambios")
            }

            when (updateState) {
                is ApiResponse.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }

                is ApiResponse.Success -> {
                    LaunchedEffect(Unit) {
                        snackbarHostState.showSnackbar("Tutorial actualizado con éxito")
                        viewModel.resetUpdateState()
                        navController.popBackStack()
                    }
                }

                is ApiResponse.Failure -> {
                    val message = (updateState as ApiResponse.Failure).errorMessage
                    LaunchedEffect(message) {
                        snackbarHostState.showSnackbar("Error al actualizar tutorial: $message")
                        viewModel.resetUpdateState()
                    }
                }

                ApiResponse.Idle -> {
                }
            }
        }
    }
}
