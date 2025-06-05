package com.jsborbon.reparalo.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.jsborbon.reparalo.components.tutorial.TutorialCard
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.navigation.Routes
import com.jsborbon.reparalo.viewmodels.TutorialsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    navController: NavController,
    auth: FirebaseAuth = FirebaseAuth.getInstance(),
) {
    val viewModel: TutorialsViewModel = viewModel()

    val favoriteIds = viewModel.favoriteIds.collectAsState().value
    val tutorialsState = viewModel.tutorials.collectAsState().value
    val currentUserId = auth.currentUser?.uid

    LaunchedEffect(Unit) {
        viewModel.loadFavorites()
        if (tutorialsState !is ApiResponse.Success) {
            viewModel.loadTutorials()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Mis Favoritos") },
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            when (val state = tutorialsState) {
                is ApiResponse.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is ApiResponse.Failure -> {
                    Text(
                        text = "Error al cargar los favoritos: ${state.errorMessage}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                is ApiResponse.Success -> {
                    val favorites = state.data.filter { it.id in favoriteIds }
                    if (favorites.isEmpty()) {
                        Text(
                            text = "Aún no tienes tutoriales marcados como favoritos.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.align(Alignment.Center),
                        )
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            items(favorites, key = { it.id }) { tutorial ->
                                TutorialCard(
                                    tutorial = tutorial,
                                    isFavorite = true,
                                    onFavoriteClick = { viewModel.toggleFavorite(tutorial.id) },
                                    onDeleteClick = null,
                                    onItemClick = {
                                        navController.navigate("${Routes.TUTORIAL_DETAIL}/${tutorial.id}")
                                    },
                                    currentUserId = currentUserId,
                                )
                            }
                        }
                    }
                }

                else -> {
                    Text(
                        text = "Cargando favoritos...",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}
