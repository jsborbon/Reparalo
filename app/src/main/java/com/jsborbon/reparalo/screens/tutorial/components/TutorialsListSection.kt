package com.jsborbon.reparalo.screens.tutorial.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.jsborbon.reparalo.components.tutorial.TutorialCard
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.Tutorial

@Composable
fun TutorialsListSection(
    tutorialsState: ApiResponse<List<Tutorial>>,
    searchQuery: String,
    triggerSearch: Boolean,
    onSearchConsumed: () -> Unit,
    onFavoriteClick: (String) -> Unit,
    onDeleteClick: ((String) -> Unit)? = null,
    onItemClick: (String) -> Unit,
    favoriteIds: Set<String>,
    onReload: () -> Unit,
    auth: FirebaseAuth = FirebaseAuth.getInstance(),
) {
    val currentUserId = auth.currentUser?.uid

    when (tutorialsState) {
        is ApiResponse.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is ApiResponse.Failure -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Error al cargar tutoriales")
                    Text(tutorialsState.errorMessage, color = MaterialTheme.colorScheme.error)
                    Button(onClick = onReload) {
                        Text("Reintentar")
                    }
                }
            }
        }

        is ApiResponse.Success -> {
            val filtered = if (triggerSearch) {
                onSearchConsumed()
                val query = searchQuery.trim().lowercase()
                tutorialsState.data.filter {
                    query.isBlank() || it.title.lowercase().contains(query) ||
                        it.description.lowercase().contains(query) ||
                        it.author.name.lowercase().contains(query)
                }
            } else {
                tutorialsState.data
            }

            if (filtered.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No se encontraron tutoriales")
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(filtered, key = { it.id }) { tutorial ->
                        TutorialCard(
                            tutorial = tutorial,
                            isFavorite = favoriteIds.contains(tutorial.id),
                            onFavoriteClick = { onFavoriteClick(tutorial.id) },
                            onDeleteClick = {
                                onDeleteClick?.invoke(tutorial.id)
                            },
                            onItemClick = { onItemClick(tutorial.id) },
                            currentUserId = currentUserId,
                        )
                    }
                }
            }
        }

        ApiResponse.Idle -> {
        }
    }
}
