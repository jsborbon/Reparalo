package com.jsborbon.reparalo.screens.tutorial.components

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jsborbon.reparalo.components.comment.CommentCard
import com.jsborbon.reparalo.components.video.FullScreenVideoActivity
import com.jsborbon.reparalo.components.video.VideoPlayer
import com.jsborbon.reparalo.models.Comment
import com.jsborbon.reparalo.models.Material
import com.jsborbon.reparalo.models.Tutorial
import com.jsborbon.reparalo.viewmodels.TutorialsViewModel

@Composable
fun TutorialDetailContent(
    tutorial: Tutorial,
    commentsState: List<Comment>,
    innerPadding: PaddingValues,
    tutorialId: String,
    userNames: Map<String, String>,
) {
    val tutorialsViewModel: TutorialsViewModel = viewModel()
    val context = LocalContext.current
    val materials = remember(tutorial.materials) { tutorial.materials }
    val comments = remember(commentsState) { commentsState }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            TutorialHeaderSection(
                tutorial = tutorial,
                tutorialId = tutorialId,
                favorites = tutorialsViewModel.favoriteIds.collectAsState().value,
                onToggleFavorite = { tutorialsViewModel.toggleFavorite(tutorialId) },
            )
        }

        if (tutorial.videoUrl.isNotBlank()) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    VideoPlayer(
                        videoUrl = tutorial.videoUrl,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Button(
                        onClick = {
                            val intent = Intent(context, FullScreenVideoActivity::class.java).apply {
                                putExtra("video_url", tutorial.videoUrl)
                            }
                            context.startActivity(intent)
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Ver en pantalla completa")
                    }
                }
            }
        }

        if (materials.isNotEmpty()) {
            item {
                Text(
                    text = "Materiales necesarios",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            items(materials, key = { it.name }) { material: Material ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = material.name, fontWeight = FontWeight.Bold)
                        Text(text = material.description)
                        Text(text = "Cantidad: ${material.quantity}")
                        Text(text = "Precio: ${material.price} €")
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Comentarios",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
        }

        items(comments, key = { it.id }) { comment ->
            val authorName = remember(comment.author.uid, userNames) {
                userNames[comment.author.uid] ?: "Usuario"
            }
            CommentCard(comment = comment, userName = authorName)
        }
    }
}
