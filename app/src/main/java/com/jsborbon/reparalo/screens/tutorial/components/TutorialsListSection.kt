package com.jsborbon.reparalo.screens.tutorial.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.jsborbon.reparalo.R
import com.jsborbon.reparalo.components.states.EmptyState
import com.jsborbon.reparalo.components.states.ErrorState
import com.jsborbon.reparalo.components.states.IdleState
import com.jsborbon.reparalo.components.states.LoadingState
import com.jsborbon.reparalo.components.tutorial.TutorialCard
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.Tutorial

/**
 * Enhanced tutorial listing section with improved state handling,
 * animations, and accessibility.
 *
 * @param tutorialsState The current state of tutorials data from API
 * @param searchQuery Current search query text
 * @param triggerSearch Flag to indicate search should be performed
 * @param onSearchConsumed Callback when search is processed
 * @param onFavoriteClick Callback when a tutorial is favorited
 * @param onDeleteClick Optional callback when a tutorial is deleted
 * @param onItemClick Callback when a tutorial is selected
 * @param favoriteIds Set of tutorial IDs marked as favorites
 * @param onReload Callback to reload tutorials data
 * @param auth Firebase authentication instance
 */
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
    val listState = rememberLazyListState()
    val isSearching = searchQuery.isNotBlank()

    // Container with semantic description for accessibility
    Box(
        modifier = Modifier
            .fillMaxSize()
            .semantics {
                contentDescription = "Lista de tutoriales"
            }
            .testTag("tutorialsListSection")
    ) {
        when (tutorialsState) {
            is ApiResponse.Loading -> {
                LoadingState(message = "Cargando tutoriales...")
            }

            is ApiResponse.Failure -> {
                ErrorState(
                    title = "Error al cargar tutoriales",
                    message = tutorialsState.errorMessage,
                    onRetry = onReload
                )
            }

            is ApiResponse.Success -> {
                val filtered = if (triggerSearch) {
                    onSearchConsumed()
                    filterTutorials(tutorialsState.data, searchQuery)
                } else {
                    tutorialsState.data
                }

                if (filtered.isEmpty()) {
                    if (isSearching) {
                        EmptyState(
                            isSearching = true,
                            searchQuery = searchQuery,
                            onClearSearch = { onSearchConsumed() }
                        )
                    } else {
                        IdleState(
                           painter = painterResource(id=R.drawable.outline_library_books),
                            title = "Aún no hay tutoriales",
                            message = "Pronto añadiremos más contenido para ayudarte con tus reparaciones.",
                            semanticDescription = "No hay tutoriales disponibles"
                        )
                    }
                } else {
                    TutorialsList(
                        tutorials = filtered,
                        listState = listState,
                        favoriteIds = favoriteIds,
                        currentUserId = currentUserId,
                        onFavoriteClick = onFavoriteClick,
                        onDeleteClick = onDeleteClick,
                        onItemClick = onItemClick
                    )
                }
            }

            ApiResponse.Idle -> {
                IdleState(
                    icon = Icons.Default.Search,
                    title = "Busca tutoriales",
                    message = "Utiliza la barra de búsqueda para encontrar tutoriales por título, descripción o autor.",
                    semanticDescription = "Estado inicial de búsqueda de tutoriales"
                )
            }
        }
    }
}

/**
 * Filtered tutorials based on search query
 */
private fun filterTutorials(tutorials: List<Tutorial>, query: String): List<Tutorial> {
    if (query.isBlank()) return tutorials

    val normalizedQuery = query.trim().lowercase()
    return tutorials.filter {
        it.title.lowercase().contains(normalizedQuery) ||
            it.description.lowercase().contains(normalizedQuery) ||
            it.author.name.lowercase().contains(normalizedQuery) ||
            it.category.lowercase().contains(normalizedQuery)
    }
}

/**
 * Animated list of tutorials with staggered entrance
 */
@Composable
private fun TutorialsList(
    tutorials: List<Tutorial>,
    listState: LazyListState,
    favoriteIds: Set<String>,
    currentUserId: String?,
    onFavoriteClick: (String) -> Unit,
    onDeleteClick: ((String) -> Unit)?,
    onItemClick: (String) -> Unit
) {
    val visibleState = remember {
        MutableTransitionState(false).apply { targetState = true }
    }

    AnimatedVisibility(
        visibleState = visibleState,
        enter = fadeIn(tween(300)),
        exit = fadeOut(tween(300))
    ) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                items = tutorials,
                key = { it.id }
            ) { tutorial ->
                // Individual item animation
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(tween(300)) +
                        slideInVertically(
                            initialOffsetY = { it * 2 },
                            animationSpec = tween(durationMillis = 400)
                        ),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    TutorialCard(
                        tutorial = tutorial,
                        isFavorite = favoriteIds.contains(tutorial.id),
                        onFavoriteClick = { onFavoriteClick(tutorial.id) },
                        onDeleteClick = if (tutorial.author.uid == currentUserId && onDeleteClick != null) {
                            { onDeleteClick(tutorial.id) }
                        } else null,
                        onItemClick = { onItemClick(tutorial.id) },
                        currentUserId = currentUserId,
                    )
                }
            }
        }
    }
}
