package com.jsborbon.reparalo.screens.tutorial

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.Author
import com.jsborbon.reparalo.models.Material
import com.jsborbon.reparalo.models.Tutorial
import com.jsborbon.reparalo.screens.tutorial.components.EnhancedTextField
import com.jsborbon.reparalo.ui.components.LoadingIndicator
import com.jsborbon.reparalo.ui.theme.Error
import com.jsborbon.reparalo.ui.theme.PrimaryLight
import com.jsborbon.reparalo.ui.theme.Success
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

    var tutorialTitle by remember { mutableStateOf("") }
    var tutorialDescription by remember { mutableStateOf("") }
    var tutorialCategory by remember { mutableStateOf("") }
    var difficultyLevel by remember { mutableStateOf("") }
    var estimatedDuration by remember { mutableStateOf("") }
    var tutorialAuthor by remember { mutableStateOf(Author()) }
    var materialsList by remember { mutableStateOf<List<Material>>(emptyList()) }
    var newMaterialName by remember { mutableStateOf("") }
    var thumbnailUrl by remember { mutableStateOf("") }
    var tutorialVideoUrl by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadTutorials()
    }

    LaunchedEffect(tutorialsState) {
        if (tutorialsState is ApiResponse.Success) {
            val selectedTutorial = (tutorialsState as ApiResponse.Success<List<Tutorial>>).data
                .find { it.id == tutorialId }

            selectedTutorial?.let { tutorial ->
                tutorialTitle = tutorial.title
                tutorialDescription = tutorial.description
                tutorialCategory = tutorial.category
                difficultyLevel = tutorial.difficultyLevel
                estimatedDuration = tutorial.estimatedDuration
                tutorialAuthor = tutorial.author
                tutorialVideoUrl = tutorial.videoUrl
                materialsList = tutorial.materials.map { materialId ->
                    Material(
                        id = materialId,
                        name = "Material $materialId",
                        quantity = 1,
                        description = "",
                        price = 0f
                    )
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))

                // Basic Information Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Información básica",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = PrimaryLight
                        )

                        EnhancedTextField(
                            value = tutorialTitle,
                            onValueChange = { tutorialTitle = it },
                            label = "Título del tutorial",
                            placeholder = "Ej: Reparar grifo que gotea"
                        )

                        EnhancedTextField(
                            value = tutorialDescription,
                            onValueChange = { tutorialDescription = it },
                            label = "Descripción",
                            placeholder = "Describe paso a paso el proceso de reparación",
                            maxLines = 4
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            EnhancedTextField(
                                value = tutorialCategory,
                                onValueChange = { tutorialCategory = it },
                                label = "Categoría",
                                placeholder = "Fontanería",
                                modifier = Modifier.weight(1f)
                            )

                            EnhancedTextField(
                                value = difficultyLevel,
                                onValueChange = { difficultyLevel = it },
                                label = "Dificultad",
                                placeholder = "Fácil",
                                modifier = Modifier.weight(1f)
                            )
                        }

                        EnhancedTextField(
                            value = estimatedDuration,
                            onValueChange = { estimatedDuration = it },
                            label = "Duración estimada",
                            placeholder = "30 minutos"
                        )

                        EnhancedTextField(
                            value = tutorialAuthor.name,
                            onValueChange = { tutorialAuthor = tutorialAuthor.copy(name = it) },
                            label = "Nombre del autor",
                            placeholder = "Tu nombre completo"
                        )
                    }
                }
            }

            item {
                // Media Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Multimedia",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = PrimaryLight
                        )

                        EnhancedTextField(
                            value = thumbnailUrl,
                            onValueChange = { thumbnailUrl = it },
                            label = "URL de imagen miniatura",
                            placeholder = "https://ejemplo.com/imagen.jpg"
                        )

                        EnhancedTextField(
                            value = tutorialVideoUrl,
                            onValueChange = { tutorialVideoUrl = it },
                            label = "URL del video tutorial",
                            placeholder = "https://youtube.com/watch?v=..."
                        )
                    }
                }
            }

            item {
                // Materials Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Materiales necesarios",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = PrimaryLight
                        )

                        if (materialsList.isNotEmpty()) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                materialsList.forEach { material ->
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.surface
                                        ),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(12.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {
                                            Text(
                                                text = material.name,
                                                style = MaterialTheme.typography.bodyMedium,
                                                modifier = Modifier.weight(1f)
                                            )
                                            IconButton(
                                                onClick = {
                                                    materialsList =
                                                        materialsList.filterNot { it.id == material.id }
                                                },
                                                modifier = Modifier.semantics {
                                                    contentDescription =
                                                        "Eliminar material ${material.name}"
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Delete,
                                                    contentDescription = "Eliminar material",
                                                    tint = Error
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.Bottom
                        ) {
                            EnhancedTextField(
                                value = newMaterialName,
                                onValueChange = { newMaterialName = it },
                                label = "Nuevo material",
                                placeholder = "Ej: Llave inglesa",
                                modifier = Modifier.weight(1f)
                            )

                            FilledTonalButton(
                                onClick = {
                                    if (newMaterialName.isNotBlank()) {
                                        val newMaterial = Material(
                                            id = System.currentTimeMillis().toString(),
                                            name = newMaterialName.trim(),
                                            quantity = 1,
                                            description = "",
                                            price = 0f,
                                        )
                                        materialsList = materialsList + newMaterial
                                        newMaterialName = ""
                                    }
                                },
                                enabled = newMaterialName.isNotBlank(),
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = PrimaryLight.copy(alpha = 0.2f),
                                    contentColor = PrimaryLight
                                ),
                                modifier = Modifier.semantics {
                                    contentDescription = "Añadir nuevo material a la lista"
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.size(4.dp))
                                Text("Añadir")
                            }
                        }
                    }
                }
            }

            item {
                AnimatedVisibility(
                    visible = updateState !is ApiResponse.Loading,
                    enter = scaleIn(animationSpec = tween(200)),
                    exit = scaleOut(animationSpec = tween(200))
                ) {
                    Button(
                        onClick = {
                            val updatedTutorial = Tutorial(
                                id = tutorialId,
                                title = tutorialTitle,
                                description = tutorialDescription,
                                category = tutorialCategory,
                                difficultyLevel = difficultyLevel,
                                estimatedDuration = estimatedDuration,
                                author = tutorialAuthor,
                                materials = materialsList.map { it.id },
                                videoUrl = tutorialVideoUrl,
                                publicationDate = Date(),
                                averageRating = 0f,
                            )
                            viewModel.updateTutorial(tutorialId, updatedTutorial)
                        },
                        enabled = tutorialTitle.isNotBlank() && tutorialDescription.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Success,
                            contentColor = Color.White,
                            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics { contentDescription = "Guardar cambios del tutorial" }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = "Guardar cambios",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            item {
                AnimatedVisibility(
                    visible = updateState is ApiResponse.Loading,
                    enter = fadeIn(animationSpec = tween(300)),
                    exit = fadeOut(animationSpec = tween(200))
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingIndicator()
                    }
                }
            }
        }

        when (updateState) {
            is ApiResponse.Success -> {
                LaunchedEffect(Unit) {
                    snackbarHostState.showSnackbar("Tutorial actualizado exitosamente")
                    viewModel.resetUpdateState()
                    navController.popBackStack()
                }
            }

            is ApiResponse.Failure -> {
                val errorMessage = (updateState as ApiResponse.Failure).errorMessage
                LaunchedEffect(errorMessage) {
                    snackbarHostState.showSnackbar("Error: $errorMessage")
                    viewModel.resetUpdateState()
                }
            }

            else -> {}
        }
    }
}
