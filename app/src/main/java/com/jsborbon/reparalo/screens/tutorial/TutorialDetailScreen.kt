package com.jsborbon.reparalo.screens.tutorial

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jsborbon.reparalo.components.comments.CommentFormSection
import com.jsborbon.reparalo.components.comments.CommentListSection
import com.jsborbon.reparalo.components.video.VideoPlayer
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.Comment
import com.jsborbon.reparalo.models.Tutorial
import com.jsborbon.reparalo.ui.theme.RepairYellow
import com.jsborbon.reparalo.viewmodels.TutorialDetailViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialDetailScreen(
    navController: NavController,
    tutorialId: String
) {
    val viewModel: TutorialDetailViewModel = viewModel()
    val tutorialState by viewModel.tutorial.collectAsState()
    val commentsState by viewModel.comments.collectAsState()

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val visibleState = remember { mutableStateOf(false) }

    key(currentRoute) {
        LaunchedEffect(Unit) {
            viewModel.loadTutorial(tutorialId)
            viewModel.loadComments(tutorialId)
            visibleState.value = true
        }

        AnimatedVisibility(
            visible = visibleState.value,
            enter = slideInHorizontally { it } + fadeIn(),
            exit = slideOutHorizontally { -it } + fadeOut()
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Detalle del Tutorial") },
                        navigationIcon = {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Regresar"
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = { /* Share */ }) {
                                Icon(Icons.Default.Share, contentDescription = "Compartir")
                            }
                            IconButton(onClick = { /* Favorite */ }) {
                                Icon(Icons.Default.Favorite, contentDescription = "Favorito")
                            }
                        }
                    )
                }
            ) { innerPadding ->
                when (val state = tutorialState) {
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
                                Text("Error al cargar el tutorial")
                                Text(state.errorMessage, color = MaterialTheme.colorScheme.error)
                                Button(onClick = { viewModel.loadTutorial(tutorialId) }) {
                                    Text("Reintentar")
                                }
                            }
                        }
                    }

                    is ApiResponse.Success -> {
                        TutorialDetailContent(
                            tutorial = state.data,
                            commentsState = commentsState,
                            viewModel = viewModel,
                            innerPadding = innerPadding,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TutorialDetailContent(
    tutorial: Tutorial,
    commentsState: ApiResponse<List<Comment>>,
    viewModel: TutorialDetailViewModel,
    innerPadding: PaddingValues,
    navController: NavController
) {
    val commentText = remember { mutableStateOf("") }
    val rating = remember { mutableIntStateOf(5) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            if (tutorial.videoUrl.isNotBlank()) {
                VideoPlayer(videoUrl = tutorial.videoUrl)
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .background(Color.DarkGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay video disponible", color = Color.White)
                }
            }
        }

        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = tutorial.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = tutorial.category,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("•", color = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = tutorial.difficultyLevel,
                        color = when (tutorial.difficultyLevel) {
                            "Fácil" -> Color.Green
                            "Intermedio" -> RepairYellow
                            "Avanzado" -> Color.Red
                            else -> Color.Gray
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = RepairYellow)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = String.format(Locale.getDefault(), "%.1f", tutorial.averageRating),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text("Duración: ${tutorial.estimatedDuration}", color = Color.Gray)
                Text("Por: ${tutorial.author}", color = Color.Gray)

                Spacer(modifier = Modifier.height(16.dp))
                Text("Descripción", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(tutorial.description)
            }
        }

        if (tutorial.materials.isNotEmpty()) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Materiales necesarios", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            tutorial.materials.forEachIndexed { index, material ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("${material.name} (${material.quantity})")
                                }
                                if (index < tutorial.materials.size - 1) Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }

        item {
            CommentFormSection(
                commentText = commentText,
                rating = rating,
                onSubmit = {
                    if (commentText.value.isNotBlank()) {
                        viewModel.submitComment(
                            tutorialId = tutorial.id,
                            text = commentText.value,
                            rating = rating.intValue
                        )
                        commentText.value = ""
                    }
                }
            )
        }

        item {
            CommentListSection(
                commentsState = commentsState,
                viewModel = viewModel,
                tutorialId = tutorial.id
            )
        }
    }
}
