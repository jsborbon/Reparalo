package com.jsborbon.reparalo.screens.tutorial

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.jsborbon.reparalo.components.states.ErrorState
import com.jsborbon.reparalo.components.states.LoadingState
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.screens.tutorial.components.DeleteTutorialDialog
import com.jsborbon.reparalo.screens.tutorial.components.TutorialDetailContent
import com.jsborbon.reparalo.viewmodels.TutorialDetailViewModel
import com.jsborbon.reparalo.viewmodels.TutorialsViewModel

@Composable
fun TutorialDetailScreen(
    navController: NavController,
    tutorialId: String
) {
    val detailViewModel: TutorialDetailViewModel = viewModel()
    val sharedViewModel: TutorialsViewModel = viewModel()
    val context = LocalContext.current
    val auth = remember { FirebaseAuth.getInstance() }
    val currentUserId = auth.currentUser?.uid

    val tutorialState by detailViewModel.tutorial.collectAsState()
    val commentsState by detailViewModel.comments.collectAsState()
    val materialsState by detailViewModel.materials.collectAsState()
    val userNames by detailViewModel.userNames.collectAsState()
    val deleteState by sharedViewModel.deleteState.collectAsState()

    val showDeleteDialog = remember { mutableStateOf(false) }
    val commentText = remember { mutableStateOf("") }
    val rating = remember { mutableIntStateOf(0) }

    LaunchedEffect(tutorialId) {
        detailViewModel.loadTutorial(tutorialId)
        detailViewModel.loadComments(tutorialId)
    }

    LaunchedEffect(deleteState) {
        when (val current = deleteState) {
            is ApiResponse.Success -> {
                Toast.makeText(context, "Tutorial eliminado exitosamente", Toast.LENGTH_SHORT).show()
                sharedViewModel.resetDeleteState()
                navController.popBackStack()
            }
            is ApiResponse.Failure -> {
                Toast.makeText(context, "Error: ${current.errorMessage}", Toast.LENGTH_LONG).show()
                sharedViewModel.resetDeleteState()
            }
            else -> {}
        }
    }

    Scaffold(
        modifier = Modifier.semantics {
            contentDescription = "Pantalla de detalle del tutorial"
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val currentState = tutorialState

            AnimatedVisibility(
                visible = currentState !is ApiResponse.Idle,
                enter = slideInVertically(
                    initialOffsetY = { it / 3 },
                    animationSpec = tween(400)
                ) + fadeIn(tween(400)),
                exit = slideOutVertically(
                    targetOffsetY = { -it / 3 },
                    animationSpec = tween(300)
                ) + fadeOut(tween(300)),
                modifier = Modifier.semantics { contentDescription = "Contenido de detalles del tutorial" }
            ) {
                when (currentState) {
                    is ApiResponse.Loading -> {
                        LoadingState(
                            message = "Cargando detalles del tutorial...",
                            showLinear = true,
                            semanticDescription = "Cargando información completa del tutorial"
                        )
                    }

                    is ApiResponse.Failure -> {
                        ErrorState(
                            title = "Error al cargar el tutorial",
                            message = currentState.errorMessage,
                            onRetry = { detailViewModel.loadTutorial(tutorialId) },
                            semanticDescription = "Error al cargar los detalles del tutorial"
                        )
                    }

                    is ApiResponse.Success -> {
                        TutorialDetailContent(
                            tutorial = currentState.data,
                            commentsState = (commentsState as? ApiResponse.Success)?.data ?: emptyList(),
                            materialsState = materialsState,
                            innerPadding = PaddingValues(0.dp),
                            tutorialId = tutorialId,
                            userNames = userNames
                        )
                    }

                    ApiResponse.Idle -> {}
                }
            }
        }

        if (showDeleteDialog.value) {
            DeleteTutorialDialog(
                onConfirm = {
                    sharedViewModel.deleteTutorial(tutorialId)
                    showDeleteDialog.value = false
                },
                onDismiss = { showDeleteDialog.value = false },
                tutorialId = tutorialId,
            )
        }
    }
}
