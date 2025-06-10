package com.jsborbon.reparalo.screens.technician.searching

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.jsborbon.reparalo.components.SearchBar
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.Category
import com.jsborbon.reparalo.navigation.components.NavigationBottomBar
import com.jsborbon.reparalo.screens.technician.searching.components.SpecialtyFilterSection
import com.jsborbon.reparalo.screens.technician.searching.components.TechniciansResultSection
import com.jsborbon.reparalo.viewmodels.CategoryViewModel
import com.jsborbon.reparalo.viewmodels.TechnicianListViewModel

/**
 * Screen for searching and filtering technicians by specialty and text query.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TechnicianSearchScreen(
    navController: NavHostController,
) {

    val technicianViewModel: TechnicianListViewModel = viewModel()
    val categoryViewModel: CategoryViewModel = viewModel()
    val keyboardController = LocalSoftwareKeyboardController.current

    val techniciansState by technicianViewModel.technicians.collectAsState()
    val selectedSpecialty by technicianViewModel.selectedSpecialty.collectAsState()
    val categoriesState by categoryViewModel.categories.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }

    val availableSpecialties: List<Category> = when (val result = categoriesState) {
        is ApiResponse.Success -> {
            listOf(Category(id = "all", name = "Todos")) + result.data
        }
        else -> listOf(Category(id = "all", name = "Todos"))
    }

    LaunchedEffect(Unit) {
        technicianViewModel.loadAllTechnicians()
    }

    Scaffold(

        bottomBar = {
            NavigationBottomBar(
                selectedIndex = 2,
                navController = navController,
                onTabChange = {}
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            SearchBar(
                query = searchQuery,
                onQueryChange = {
                    searchQuery = it
                    isSearchActive = it.isNotBlank()
                },
                onClearSearch = {
                    searchQuery = ""
                    isSearchActive = false
                    keyboardController?.hide()
                },
                placeholder = "Buscar por nombre o correo...",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            SpecialtyFilterSection(
                specialties = availableSpecialties,
                selectedSpecialty = selectedSpecialty,
                onSpecialtySelected = { category ->
                    if (category.name == "Todos") {
                        technicianViewModel.loadAllTechnicians()
                    } else {
                        technicianViewModel.loadTechniciansBySpecialty(category.name)
                    }
                }
            )

            AnimatedVisibility(
                visible = selectedSpecialty != null,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = { technicianViewModel.loadAllTechnicians() },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.padding(4.dp))
                        Text("Limpiar filtro")
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            TechniciansResultSection(
                techniciansState = techniciansState,
                searchQuery = searchQuery,
                isSearchActive = isSearchActive,
                onRetry = { technicianViewModel.loadAllTechnicians() }
            )
        }
    }
}

