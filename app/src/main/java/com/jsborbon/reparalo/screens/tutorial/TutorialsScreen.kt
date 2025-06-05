package com.jsborbon.reparalo.screens.tutorial

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.Category
import com.jsborbon.reparalo.navigation.Routes
import com.jsborbon.reparalo.screens.tutorial.components.CategoryChipsRow
import com.jsborbon.reparalo.screens.tutorial.components.DeleteTutorialDialog
import com.jsborbon.reparalo.screens.tutorial.components.TutorialsListSection
import com.jsborbon.reparalo.screens.tutorial.components.TutorialsSearchBar
import com.jsborbon.reparalo.viewmodels.CategoryViewModel
import com.jsborbon.reparalo.viewmodels.TutorialsViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorialsScreen(
    navController: NavController,
) {
    val viewModel: TutorialsViewModel = viewModel()
    val categoryViewModel: CategoryViewModel = viewModel()

    val allTutorials by viewModel.tutorials.collectAsState()
    val tutorialsByCategory by viewModel.tutorialsByCategory.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val deleteState by viewModel.deleteState.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()
    val categoriesState by categoryViewModel.categories.collectAsState()

    val context = LocalContext.current

    var searchQuery by remember { mutableStateOf("") }
    var triggerSearch by remember { mutableStateOf(false) }
    var tutorialToDeleteId by remember { mutableStateOf<String?>(null) }

    // ✅ Corrección: cargamos tutoriales y favoritos al iniciar
    LaunchedEffect(Unit) {
        viewModel.selectCategory(null)
        viewModel.loadFavorites()
    }

    // ✅ Mantenemos escucha de mensajes
    LaunchedEffect(Unit) {
        viewModel.uiMessage.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tutoriales") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                        )
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            if (deleteState is ApiResponse.Loading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            TutorialsSearchBar(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                onSearch = { triggerSearch = true },
            )

            when (categoriesState) {
                is ApiResponse.Loading -> {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
                is ApiResponse.Failure -> {
                    Text("Error cargando categorías")
                }
                is ApiResponse.Success -> {
                    val categories = (categoriesState as ApiResponse.Success<List<Category>>).data
                    CategoryChipsRow(
                        categories = categories,
                        selectedCategory = selectedCategory,
                        onCategorySelected = { category ->
                            viewModel.selectCategory(
                                if (selectedCategory == category.name) null else category.name,
                            )
                        },
                    )
                }
                ApiResponse.Idle -> Unit
            }

            val tutorialsState = if (selectedCategory != null) tutorialsByCategory else allTutorials

            TutorialsListSection(
                tutorialsState = tutorialsState,
                searchQuery = searchQuery,
                triggerSearch = triggerSearch,
                onSearchConsumed = { triggerSearch = false },
                onFavoriteClick = { viewModel.toggleFavorite(it) },
                onDeleteClick = { tutorialToDeleteId = it },
                onItemClick = { navController.navigate("${Routes.TUTORIAL_DETAIL}/$it") },
                favoriteIds = favoriteIds,
                onReload = { viewModel.loadTutorials() },
            )
        }
    }

    DeleteTutorialDialog(
        tutorialId = tutorialToDeleteId,
        onConfirm = {
            if (it != null) viewModel.deleteTutorial(it)
            tutorialToDeleteId = null
        },
        onDismiss = { tutorialToDeleteId = null },
    )
}
