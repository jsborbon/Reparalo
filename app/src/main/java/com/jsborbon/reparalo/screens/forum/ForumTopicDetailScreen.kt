package com.jsborbon.reparalo.screens.forum

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jsborbon.reparalo.R
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.utils.formatDate
import com.jsborbon.reparalo.viewmodels.ForumViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumTopicDetailScreen(
    navController: NavController,
    topicId: String,
) {
    val viewModel: ForumViewModel = viewModel()
    val topicsState by viewModel.topics.collectAsState()

    val topic = when (val state = topicsState) {
        is ApiResponse.Success -> state.data.find { it.id == topicId }
        else -> null
    }

    LaunchedEffect(Unit) {
        if (topicsState !is ApiResponse.Success) {
            viewModel.loadTopics()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Tema") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Volver",
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            when (topicsState) {
                is ApiResponse.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is ApiResponse.Failure -> {
                    Text(
                        text = "No se pudo cargar el tema.",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                is ApiResponse.Success -> {
                    if (topic == null) {
                        Text(
                            text = "El tema no fue encontrado.",
                            modifier = Modifier.align(Alignment.Center),
                        )
                    } else {
                        TopicDetailContent(topic = topic)
                    }
                }
            }
        }
    }
}

@Composable
private fun TopicDetailContent(topic: com.jsborbon.reparalo.models.ForumTopic) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp),
    ) {
        Text(
            text = topic.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Publicado por ${topic.author} el ${formatDate(topic.date.time)}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = topic.description,
            style = MaterialTheme.typography.bodyLarge,
        )

        Spacer(modifier = Modifier.height(24.dp))

        ForumStatsRow(
            views = topic.views,
            likes = topic.likes,
            comments = topic.comments,
        )
    }
}

@Composable
private fun ForumStatsRow(
    views: Int,
    likes: Int,
    comments: Int,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
    ) {
        ForumStatItemPainter(
            icon = painterResource(id = R.drawable.baseline_visibility),
            label = "Vistas",
            value = views,
        )
        ForumStatItemVector(
            icon = Icons.Default.ThumbUp,
            label = "Me gusta",
            value = likes,
        )
        ForumStatItemPainter(
            icon = painterResource(id = R.drawable.baseline_comment),
            label = "Comentarios",
            value = comments,
        )
    }
}

@Composable
private fun ForumStatItemVector(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: Int,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = "$value $label",
            modifier = Modifier.padding(start = 8.dp),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun ForumStatItemPainter(
    icon: androidx.compose.ui.graphics.painter.Painter,
    label: String,
    value: Int,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp),
    ) {
        Icon(
            painter = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = "$value $label",
            modifier = Modifier.padding(start = 8.dp),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
