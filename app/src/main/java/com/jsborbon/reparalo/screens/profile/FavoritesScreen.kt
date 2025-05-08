package com.jsborbon.reparalo.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.Tutorial
import com.jsborbon.reparalo.navigation.Routes
import com.jsborbon.reparalo.screens.tutorial.TutorialCard
import com.jsborbon.reparalo.viewmodels.FavoritesViewModel

@Composable
fun FavoritesScreen(
    navController: NavController,
    viewModel: FavoritesViewModel = viewModel()
) {
    val state by viewModel.favoriteTutorials.collectAsState()
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    LaunchedEffect(userId) {
        userId?.let { viewModel.fetchFavorites(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Mis Favoritos",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        when (state) {
            is ApiResponse.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }

            is ApiResponse.Success -> {
                val tutorials = (state as ApiResponse.Success<List<Tutorial>>).data
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(tutorials) { tutorial ->
                        TutorialCard(
                            tutorial = tutorial,
                            onClick = {
                                navController.navigate("${Routes.TUTORIAL_DETAIL}/${tutorial.id}")
                            }
                        )
                    }
                }
            }

            is ApiResponse.Failure -> {
                val message = (state as ApiResponse.Failure).errorMessage
                Text(
                    text = "Error al cargar favoritos: $message",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
