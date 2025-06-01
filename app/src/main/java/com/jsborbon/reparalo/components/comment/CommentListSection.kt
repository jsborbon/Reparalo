package com.jsborbon.reparalo.components.comment

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.Comment
import com.jsborbon.reparalo.viewmodels.TutorialDetailViewModel

@Composable
fun CommentListSection(
    commentsState: ApiResponse<List<Comment>>,
    viewModel: TutorialDetailViewModel,
    tutorialId: String,
) {
    val userNames = viewModel.userNames.collectAsState().value

    when (commentsState) {
        is ApiResponse.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }

        is ApiResponse.Failure -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Error al cargar comentarios")
                    Text(
                        text = commentsState.errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { viewModel.loadComments(tutorialId) }) {
                        Text(text = "Reintentar")
                    }
                }
            }
        }

        is ApiResponse.Success -> {
            val comments = commentsState.data
            if (comments.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "Sé el primero en comentar este tutorial.",
                        color = Color.Gray,
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                ) {
                    items(comments) { comment ->
                        CommentCard(
                            comment = comment,
                            userName = userNames[comment.author.uid] ?: "Usuario anónimo",
                        )
                    }
                }
            }
        }
    }
}
