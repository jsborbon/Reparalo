package com.jsborbon.reparalo.screens.tutorial

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.jsborbon.reparalo.components.comment.CommentFormSection
import com.jsborbon.reparalo.components.comment.CommentListSection
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.screens.tutorial.components.TutorialDetailContent
import com.jsborbon.reparalo.ui.components.LoadingIndicator
import com.jsborbon.reparalo.ui.theme.Error
import com.jsborbon.reparalo.ui.theme.PrimaryLight
import com.jsborbon.reparalo.ui.theme.RepairYellow
import com.jsborbon.reparalo.viewmodels.TutorialDetailViewModel
import com.jsborbon.reparalo.viewmodels.TutorialsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialDetailScreen(
    navController: NavController,
    tutorialId: String,
) {
    val detailViewModel: TutorialDetailViewModel = viewModel()
    val sharedViewModel: TutorialsViewModel = viewModel()
    val auth = remember { FirebaseAuth.getInstance() }
    val currentUserId = auth.currentUser?.uid ?: throw IllegalStateException("Usuario no autenticado")
    val context = LocalContext.current

    val tutorialState by detailViewModel.tutorial.collectAsState()
    val commentsState by detailViewModel.comments.collectAsState()
    val materialsState by detailViewModel.materials.collectAsState()
    val userNames by detailViewModel.userNames.collectAsState()
    val deleteState = sharedViewModel.deleteState.collectAsState().value

    val showDeleteDialog = remember { mutableStateOf(false) }
    val commentText = remember { mutableStateOf("") }
    val rating = remember { mutableIntStateOf(0) }

    LaunchedEffect(tutorialId) {
        detailViewModel.loadTutorial(tutorialId)
        detailViewModel.loadComments(tutorialId)
    }

    LaunchedEffect(deleteState) {
        when (deleteState) {
            is ApiResponse.Success -> {
                Toast.makeText(context, "Tutorial eliminado exitosamente", Toast.LENGTH_SHORT).show()
                sharedViewModel.resetDeleteState()
                navController.popBackStack()
            }
            is ApiResponse.Failure -> {
                Toast.makeText(context, "Error: ${deleteState.errorMessage}", Toast.LENGTH_LONG).show()
                sharedViewModel.resetDeleteState()
            }
            else -> {}
        }
    }

    Scaffold {
            paddingValues ->

        AnimatedVisibility(
            visible = tutorialState !is ApiResponse.Idle,
            enter = slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(durationMillis = 300)
            ) + fadeIn(animationSpec = tween(durationMillis = 300)),
            exit = slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(durationMillis = 200)
            ) + fadeOut(animationSpec = tween(durationMillis = 200))
        ) {
            when (val state = tutorialState) {
                is ApiResponse.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingIndicator()
                    }
                }
                is ApiResponse.Failure -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            modifier = Modifier.padding(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f)
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = "Error",
                                    tint = Error,
                                    modifier = Modifier.size(48.dp)
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "Error al cargar el tutorial",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = state.errorMessage,
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center,
                                    color = Error
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    onClick = { detailViewModel.loadTutorial(tutorialId) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = PrimaryLight,
                                        contentColor = Color.White
                                    ),
                                    modifier = Modifier.semantics {
                                        contentDescription = "Intentar cargar el tutorial nuevamente"
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.size(8.dp))
                                    Text("Reintentar")
                                }
                            }
                        }
                    }
                }
                is ApiResponse.Success -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        item {
                            TutorialDetailContent(
                                tutorial = state.data,
                                commentsState = (commentsState as? ApiResponse.Success)?.data ?: emptyList(),
                                materialsState = materialsState,
                                innerPadding = paddingValues,
                                tutorialId = tutorialId,
                                userNames = userNames
                            )
                        }

                        item {
                            Card(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(
                                        text = "Comentarios y valoraciones",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(bottom = 16.dp)
                                    )

                                    CommentFormSection(
                                        commentText = commentText,
                                        rating = rating,
                                        onSubmit = {
                                            detailViewModel.submitComment(
                                                tutorialId = tutorialId,
                                                content = commentText.value,
                                                rating = rating.intValue,
                                                userId = currentUserId
                                            )
                                            commentText.value = ""
                                            rating.intValue = 0
                                            Toast.makeText(context, "Comentario enviado correctamente", Toast.LENGTH_SHORT).show()
                                        }
                                    )
                                }
                            }
                        }

                        item {
                            CommentListSection(
                                commentsState = commentsState,
                                userNamesMap = userNames,
                                onRetry = { detailViewModel.loadComments(tutorialId) }
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }
                ApiResponse.Idle -> {}
            }
        }

        if (showDeleteDialog.value) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog.value = false },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Advertencia",
                        tint = RepairYellow,
                        modifier = Modifier.size(32.dp)
                    )
                },
                title = {
                    Text(
                        text = "Eliminar tutorial",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                text = {
                    Text(
                        text = "¿Estás seguro de que deseas eliminar este tutorial? Esta acción no se puede deshacer y se perderá toda la información asociada.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            sharedViewModel.deleteTutorial(tutorialId)
                            showDeleteDialog.value = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Error,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.semantics {
                            contentDescription = "Confirmar eliminación del tutorial"
                        }
                    ) {
                        Text(
                            text = "Eliminar",
                            fontWeight = FontWeight.Medium
                        )
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = { showDeleteDialog.value = false },
                        modifier = Modifier.semantics {
                            contentDescription = "Cancelar eliminación"
                        }
                    ) {
                        Text(
                            text = "Cancelar",
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            )
        }
    }
}
