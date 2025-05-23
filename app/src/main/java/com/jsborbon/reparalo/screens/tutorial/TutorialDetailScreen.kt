package com.jsborbon.reparalo.screens.tutorial

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.viewmodels.TutorialDetailViewModel
import com.jsborbon.reparalo.viewmodels.TutorialsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialDetailScreen(
    navController: NavController,
    tutorialId: String,
    sharedViewModel: TutorialsViewModel = hiltViewModel(),
) {
    val detailViewModel: TutorialDetailViewModel = hiltViewModel()
    val tutorialState by detailViewModel.tutorial.collectAsState()
    val commentsState by detailViewModel.comments.collectAsState()
    val deleteState by sharedViewModel.deleteState.collectAsState()
    val favoriteState by sharedViewModel.isFavorite.collectAsState()
    val isFavorite = (favoriteState as? ApiResponse.Success)?.data == true
    val isLoadingFavorite = favoriteState is ApiResponse.Loading
    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        detailViewModel.loadTutorial(tutorialId)
        detailViewModel.loadComments(tutorialId)
    }

    LaunchedEffect(deleteState) {
        when (deleteState) {
            is ApiResponse.Success -> {
                Toast.makeText(context, "Tutorial eliminado con éxito", Toast.LENGTH_SHORT).show()
                sharedViewModel.resetDeleteState()
                navController.popBackStack()
            }
            is ApiResponse.Failure -> {
                Toast.makeText(context, deleteState.errorMessage, Toast.LENGTH_SHORT).show()
                sharedViewModel.resetDeleteState()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Tutorial") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                putExtra(Intent.EXTRA_TEXT, "Mira este tutorial: https://tuapp.com/tutorial/$tutorialId")
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(intent, "Compartir tutorial"))
                        },
                    ) {
                        Icon(Icons.Default.Share, contentDescription = "Compartir")
                    }
                    IconButton(onClick = { sharedViewModel.toggleFavorite(tutorialId) }, enabled = !isLoadingFavorite) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = if (isFavorite) "Quitar de favoritos" else "Agregar a favoritos",
                            tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface,
                        )
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                    }
                },
            )
        },
    ) { padding ->
        AnimatedVisibility(
            visible = true,
            enter = slideInHorizontally { it } + fadeIn(),
            exit = slideOutHorizontally { -it } + fadeOut(),
        ) {
            when (val state = tutorialState) {
                is ApiResponse.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
                is ApiResponse.Failure -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Error al cargar el tutorial")
                        Text(state.errorMessage, color = MaterialTheme.colorScheme.error)
                        Button(onClick = { detailViewModel.loadTutorial(tutorialId) }) {
                            Text("Reintentar")
                        }
                    }
                }
                is ApiResponse.Success -> TutorialDetailContent(
                    tutorial = state.data,
                    commentsState = commentsState,
                    viewModel = detailViewModel,
                    innerPadding = padding,
                )
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
                    }) { Text("Eliminar") }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) { Text("Cancelar") }
                },
            )
        }
    }
}
