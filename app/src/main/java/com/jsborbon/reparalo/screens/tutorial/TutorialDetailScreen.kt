package com.jsborbon.reparalo.screens.tutorial

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jsborbon.reparalo.components.comments.CommentFormSection
import com.jsborbon.reparalo.components.comments.CommentListSection
import com.jsborbon.reparalo.components.video.VideoPlayer
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.api.ApiResponse.Success
import com.jsborbon.reparalo.models.Comment
import com.jsborbon.reparalo.models.Tutorial
import com.jsborbon.reparalo.ui.theme.RepairYellow
import com.jsborbon.reparalo.viewmodels.TutorialDetailViewModel
import com.jsborbon.reparalo.viewmodels.TutorialsViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialDetailScreen(
    navController: NavController,
    tutorialId: String,
    sharedViewModel: TutorialsViewModel = hiltViewModel()
) {
    val detailViewModel: TutorialDetailViewModel = hiltViewModel()
    val tutorialState by detailViewModel.tutorial.collectAsState()
    val commentsState by detailViewModel.comments.collectAsState()
    val deleteState by sharedViewModel.deleteState.collectAsState()
    val favoriteState by sharedViewModel.isFavorite.collectAsState()
    val isFavorite = (favoriteState as? Success<Boolean>)?.data == true
    val isLoadingFavorite = favoriteState is ApiResponse.Loading

    val context = LocalContext.current

    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(deleteState) {
        when (deleteState) {
            is Success -> {
                Toast.makeText(context, "Tutorial eliminado con éxito", Toast.LENGTH_SHORT).show()
                sharedViewModel.resetDeleteState()
                navController.popBackStack()
            }

            is ApiResponse.Failure -> {
                val errorMessage = (deleteState as ApiResponse.Failure).errorMessage
                Toast.makeText(context, "Error al eliminar tutorial: $errorMessage", Toast.LENGTH_SHORT).show()
                sharedViewModel.resetDeleteState()
            }

            else -> {}
        }
    }

    LaunchedEffect(Unit) {
        detailViewModel.loadTutorial(tutorialId)
        detailViewModel.loadComments(tutorialId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Tutorial") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            val shareIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, "Mira este tutorial: https://tuapp.com/tutorial/${tutorialId}")
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Compartir tutorial"))
                        }
                    ) {
                        Icon(Icons.Default.Share, contentDescription = "Compartir")
                    }

                    IconButton(
                        onClick = { sharedViewModel.toggleFavorite(tutorialId) },
                        enabled = !isLoadingFavorite
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = if (isFavorite) "Quitar de favoritos" else "Agregar a favoritos",
                            tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        AnimatedVisibility(
            visible = true,
            enter = slideInHorizontally { it } + fadeIn(),
            exit = slideOutHorizontally { -it } + fadeOut()
        ) {
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
                            Button(onClick = { detailViewModel.loadTutorial(tutorialId) }) {
                                Text("Reintentar")
                            }
                        }
                    }
                }

                is Success -> {
                    TutorialDetailContent(
                        tutorial = state.data,
                        commentsState = commentsState,
                        viewModel = detailViewModel,
                        innerPadding = innerPadding
                    )
                }
            }
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Eliminar tutorial") },
                text = { Text("¿Estás seguro de que deseas eliminar este tutorial? Esta acción no se puede deshacer.") },
                confirmButton = {
                    TextButton(onClick = {
                        sharedViewModel.deleteTutorial(tutorialId)
                        showDeleteDialog = false
                    }) {
                        Text("Eliminar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

@Composable
fun TutorialDetailContent(
    tutorial: Tutorial,
    commentsState: ApiResponse<List<Comment>>,
    viewModel: TutorialDetailViewModel,
    innerPadding: PaddingValues
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
                                if (index < tutorial.materials.size - 1) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
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
