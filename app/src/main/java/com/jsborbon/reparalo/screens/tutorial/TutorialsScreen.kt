package com.jsborbon.reparalo.screens.tutorial

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jsborbon.reparalo.R
import com.jsborbon.reparalo.components.SearchBar
import com.jsborbon.reparalo.components.states.ErrorState
import com.jsborbon.reparalo.components.states.IdleState
import com.jsborbon.reparalo.components.states.LoadingState
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.navigation.Routes
import com.jsborbon.reparalo.screens.tutorial.components.CategoryChipsRow
import com.jsborbon.reparalo.screens.tutorial.components.DeleteTutorialDialog
import com.jsborbon.reparalo.screens.tutorial.components.TutorialsListSection
import com.jsborbon.reparalo.ui.theme.PrimaryLight
import com.jsborbon.reparalo.viewmodels.CategoryViewModel
import com.jsborbon.reparalo.viewmodels.TutorialsViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialsScreen(navController: NavController) {
    val tutorialsViewModel: TutorialsViewModel = viewModel()
    val categoryViewModel: CategoryViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()

    val allTutorialsState by tutorialsViewModel.tutorials.collectAsState()
    val tutorialsByCategoryState by tutorialsViewModel.tutorialsByCategory.collectAsState()
    val selectedCategoryName by tutorialsViewModel.selectedCategory.collectAsState()
    val deleteOperationState by tutorialsViewModel.deleteState.collectAsState()
    val favoriteIdsSet by tutorialsViewModel.favoriteIds.collectAsState()
    val categoriesListState by categoryViewModel.categories.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    var searchQueryText by remember { mutableStateOf("") }
    var shouldTriggerSearch by remember { mutableStateOf(false) }
    var tutorialToDeleteId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        tutorialsViewModel.selectCategory(null)
        tutorialsViewModel.loadFavorites()
        categoryViewModel.loadCategories()
    }

    LaunchedEffect(Unit) {
        tutorialsViewModel.uiMessage.collectLatest { message ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message = message, withDismissAction = true)
            }
        }
    }

    LaunchedEffect(deleteOperationState) {
        when (val result = deleteOperationState) {
            is ApiResponse.Success -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Tutorial eliminado exitosamente", withDismissAction = true)
                }
                tutorialsViewModel.resetDeleteState()
            }

            is ApiResponse.Failure -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Error al eliminar: ${result.errorMessage}", withDismissAction = true)
                }
                tutorialsViewModel.resetDeleteState()
            }

            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Tutoriales de Reparación",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigateUp() },
                        modifier = Modifier
                            .size(48.dp)
                            .semantics { contentDescription = "Volver al menú principal" }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AnimatedVisibility(
                visible = deleteOperationState is ApiResponse.Loading,
                enter = slideInVertically(initialOffsetY = { -it }, animationSpec = tween(300)),
                exit = slideOutVertically(targetOffsetY = { -it }, animationSpec = tween(200))
            ) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics { contentDescription = "Eliminando tutorial, por favor espere" },
                    color = PrimaryLight,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            SearchBar(
                query = searchQueryText,
                onQueryChange = {
                    searchQueryText = it
                    shouldTriggerSearch = true
                },
                onClearSearch = {
                    searchQueryText = ""
                    shouldTriggerSearch = true
                },
                placeholder = "Buscar por título, descripción o autor",
                contentDescription = "Campo de búsqueda de tutoriales"
            )

            Spacer(modifier = Modifier.height(12.dp))

            when (val categoriesState = categoriesListState) {
                is ApiResponse.Loading -> {
                    LoadingState(
                        message = "Cargando categorías...",
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                is ApiResponse.Failure -> {
                    ErrorState(
                        message = "Error al cargar categorías.",
                        onRetry = { categoryViewModel.loadCategories() }
                    )
                }

                is ApiResponse.Success -> {
                    val categoriesList = categoriesState.data
                    if (categoriesList.isEmpty()) {
                        IdleState(
                            painter = painterResource(id = R.drawable.outline_category),
                            title = "Sin categorías",
                            message = "No se encontraron categorías disponibles.",
                            buttonText = "Recargar",
                            onAction = { categoryViewModel.loadCategories() }
                        )
                    } else {
                        CategoryChipsRow(
                            categories = categoriesList,
                            selectedCategoryId = selectedCategoryName,
                            onCategorySelected = { selected ->
                                tutorialsViewModel.selectCategory(
                                    if (selectedCategoryName == selected.name) null else selected.name
                                )
                            }
                        )
                    }
                }

                ApiResponse.Idle -> {
                    IdleState(
                        painter = painterResource(id = R.drawable.outline_category),
                        title = "Cargando datos",
                        message = "Por favor espera mientras se obtienen las categorías."
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            val currentTutorialsState = if (selectedCategoryName != null) {
                tutorialsByCategoryState
            } else {
                allTutorialsState
            }

            TutorialsListSection(
                tutorialsState = currentTutorialsState,
                searchQuery = searchQueryText,
                triggerSearch = shouldTriggerSearch,
                onSearchConsumed = { shouldTriggerSearch = false },
                onFavoriteClick = { tutorialsViewModel.toggleFavorite(it) },
                onDeleteClick = { tutorialToDeleteId = it },
                onItemClick = { tutorialId ->
                    navController.navigate("${Routes.TUTORIAL_DETAIL}/$tutorialId")
                },
                favoriteIds = favoriteIdsSet,
                onReload = {
                    tutorialsViewModel.loadTutorials()
                    categoryViewModel.loadCategories()
                }
            )
        }
    }

    DeleteTutorialDialog(
        tutorialId = tutorialToDeleteId,
        onConfirm = {
            if (it != null) {
                tutorialsViewModel.deleteTutorial(it)
            }
            tutorialToDeleteId = null
        },
        onDismiss = { tutorialToDeleteId = null },
    )
}
