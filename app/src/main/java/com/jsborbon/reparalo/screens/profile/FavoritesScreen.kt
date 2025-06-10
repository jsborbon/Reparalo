package com.jsborbon.reparalo.screens.profile

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.jsborbon.reparalo.components.states.ErrorState
import com.jsborbon.reparalo.components.states.LoadingState
import com.jsborbon.reparalo.components.tutorial.TutorialCard
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.navigation.Routes
import com.jsborbon.reparalo.ui.theme.PrimaryLight
import com.jsborbon.reparalo.ui.theme.RepairYellow
import com.jsborbon.reparalo.viewmodels.TutorialsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    navController: NavController,
    auth: FirebaseAuth = FirebaseAuth.getInstance(),
) {
    val tutorialsViewModel: TutorialsViewModel = viewModel()
    val favoriteIdsSet by tutorialsViewModel.favoriteIds.collectAsState()
    val tutorialsDataState by tutorialsViewModel.tutorials.collectAsState()
    val currentUserId = auth.currentUser?.uid
    val favoritesListState = rememberLazyListState()

    LaunchedEffect(Unit) {
        tutorialsViewModel.loadFavorites()
        if (tutorialsDataState !is ApiResponse.Success) {
            tutorialsViewModel.loadTutorials()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mis Favoritos",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigateUp() },
                        modifier = Modifier
                            .size(48.dp)
                            .semantics { contentDescription = "Volver a la pantalla de perfil" }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AnimatedContent(
                targetState = tutorialsDataState,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) togetherWith
                        fadeOut(animationSpec = tween(200))
                },
                label = "favoritesContent",
                modifier = Modifier.fillMaxSize()
            ) { currentState ->
                when (currentState) {
                    is ApiResponse.Loading -> {
                        LoadingState(
                            message = "Cargando lista de tutoriales favoritos..."
                        )
                    }

                    is ApiResponse.Failure -> {
                        ErrorState(
                            message = currentState.errorMessage,
                            onRetry = {
                                tutorialsViewModel.loadFavorites()
                                tutorialsViewModel.loadTutorials()
                            }
                        )
                    }

                    is ApiResponse.Success -> {
                        val favoriteTutorialsList = remember(currentState.data, favoriteIdsSet) {
                            currentState.data.filter { tutorial -> tutorial.id in favoriteIdsSet }
                        }

                        if (favoriteTutorialsList.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Card(
                                    modifier = Modifier.padding(32.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = RepairYellow.copy(alpha = 0.1f)
                                    ),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.FavoriteBorder,
                                            contentDescription = null,
                                            modifier = Modifier.size(72.dp),
                                            tint = RepairYellow
                                        )

                                        Spacer(modifier = Modifier.height(24.dp))

                                        Text(
                                            text = "Aún no tienes favoritos",
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            textAlign = TextAlign.Center
                                        )

                                        Spacer(modifier = Modifier.height(12.dp))

                                        Text(
                                            text = "Explora tutoriales de reparación y marca los que más te gusten para encontrarlos fácilmente aquí.",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.fillMaxWidth()
                                        )

                                        Spacer(modifier = Modifier.height(16.dp))

                                        Icon(
                                            imageVector = Icons.Default.Favorite,
                                            contentDescription = null,
                                            modifier = Modifier.size(24.dp),
                                            tint = PrimaryLight
                                        )
                                    }
                                }
                            }
                        } else {
                            LazyColumn(
                                state = favoritesListState,
                                contentPadding = PaddingValues(
                                    start = 16.dp,
                                    end = 16.dp,
                                    top = 8.dp,
                                    bottom = 24.dp
                                ),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .semantics {
                                        contentDescription = "Lista de ${favoriteTutorialsList.size} tutoriales favoritos"
                                    }
                            ) {
                                item {
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = CardDefaults.cardColors(
                                            containerColor = PrimaryLight.copy(alpha = 0.1f)
                                        ),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(16.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Favorite,
                                                contentDescription = null,
                                                tint = PrimaryLight,
                                                modifier = Modifier.size(24.dp)
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = "${favoriteTutorialsList.size} tutorial${if (favoriteTutorialsList.size != 1) "es" else ""} guardado${if (favoriteTutorialsList.size != 1) "s" else ""}",
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.SemiBold,
                                                color = PrimaryLight
                                            )
                                        }
                                    }
                                }

                                items(
                                    items = favoriteTutorialsList,
                                    key = { tutorial -> tutorial.id }
                                ) { tutorial ->
                                    AnimatedVisibility(
                                        visible = true,
                                        enter = slideInVertically(
                                            initialOffsetY = { it / 3 },
                                            animationSpec = tween(
                                                durationMillis = 300,
                                                delayMillis = (favoriteTutorialsList.indexOf(tutorial) * 50).coerceAtMost(500)
                                            )
                                        ) + fadeIn(
                                            animationSpec = tween(
                                                durationMillis = 300,
                                                delayMillis = (favoriteTutorialsList.indexOf(tutorial) * 50).coerceAtMost(500)
                                            )
                                        )
                                    ) {
                                        TutorialCard(
                                            tutorial = tutorial,
                                            isFavorite = true,
                                            onFavoriteClick = {
                                                tutorialsViewModel.toggleFavorite(tutorial.id)
                                            },
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
                    }

                    ApiResponse.Idle -> {
                        LoadingState(
                            message = "Preparando lista de favoritos..."
                        )
                    }
                }
            }
        }
    }
}
