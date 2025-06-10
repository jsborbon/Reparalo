package com.jsborbon.reparalo.screens.forum

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jsborbon.reparalo.components.comment.TopicDetailContent
import com.jsborbon.reparalo.components.states.EmptyState
import com.jsborbon.reparalo.components.states.ErrorState
import com.jsborbon.reparalo.components.states.LoadingState
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.Comment
import com.jsborbon.reparalo.viewmodels.ForumViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumTopicDetailScreen(
    navController: NavController,
    topicId: String,
) {
    val viewModel: ForumViewModel = viewModel()
    val topicsState by viewModel.topics.collectAsState()
    val commentsState by viewModel.comments.collectAsState()

    val topic = (topicsState as? ApiResponse.Success)?.data?.find { it.id == topicId }

    LaunchedEffect(Unit) {
        if (topicsState !is ApiResponse.Success) {
            viewModel.loadTopics()
        }
        viewModel.loadCommentsForForumTopic(topicId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detalle del Tema",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigateUp() },
                        modifier = Modifier
                            .semantics { contentDescription = "Volver al foro" }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                )
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (topicsState) {
                is ApiResponse.Loading, is ApiResponse.Idle -> {
                    LoadingState(
                        message = "Cargando detalles del tema...",
                        semanticDescription = "Cargando tema del foro"
                    )
                }

                is ApiResponse.Failure -> {
                    ErrorState(
                        message = "No se pudo cargar el tema",
                        onRetry = { viewModel.loadTopics() }
                    )
                }

                is ApiResponse.Success -> {
                    if (topic == null) {
                        EmptyState(
                            isSearching = false
                        )
                    } else {
                        when (commentsState) {
                            is ApiResponse.Loading, is ApiResponse.Idle -> {
                                LoadingState(
                                    message = "Cargando comentarios...",
                                    semanticDescription = "Cargando comentarios del foro"
                                )
                            }

                            is ApiResponse.Failure -> {
                                ErrorState(
                                    message = "No se pudieron cargar los comentarios",
                                    onRetry = { viewModel.loadCommentsForForumTopic(topicId) }
                                )
                            }

                            is ApiResponse.Success -> {
                                val comments = (commentsState as ApiResponse.Success<List<Comment>>).data
                                TopicDetailContent(topic = topic, comments = comments)
                            }
                        }
                    }
                }
            }
        }
    }
}
