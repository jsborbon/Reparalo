package com.jsborbon.reparalo.screens.technician.profile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.navigation.components.NavigationBottomBar
import com.jsborbon.reparalo.screens.technician.profile.components.TechnicianProfileContent
import com.jsborbon.reparalo.screens.technician.profile.components.TechnicianProfileTopBar
import com.jsborbon.reparalo.viewmodels.TechnicianViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TechnicianProfileScreen(
    technicianId: String,
    navController: NavHostController,
) {
    val viewModel: TechnicianViewModel = viewModel()
    val technicianState by viewModel.technician.collectAsState()

    var retryCount by remember { mutableIntStateOf(0) }
    var isContentVisible by remember { mutableStateOf(false) }

    LaunchedEffect(technicianId, retryCount) {
        viewModel.loadTechnician(technicianId)
    }

    LaunchedEffect(technicianState is ApiResponse.Success) {
        isContentVisible = technicianState is ApiResponse.Success
    }

    Scaffold(
        topBar = {
            TechnicianProfileTopBar(
                onNavigateBack = { navController.navigateUp() },
                onRefresh = { retryCount++ },
                isLoading = technicianState is ApiResponse.Loading
            )
        },
        bottomBar = {
            NavigationBottomBar(
                selectedIndex = 2,
                navController = navController,
                onTabChange = {},
            )
        },
    ) { innerPadding ->
        TechnicianProfileContent(
            technicianState = technicianState,
            isContentVisible = isContentVisible,
            onRetry = { retryCount++ },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}
