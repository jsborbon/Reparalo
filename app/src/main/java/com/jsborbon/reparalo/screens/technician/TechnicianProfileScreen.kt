package com.jsborbon.reparalo.screens.technician

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.navigation.components.NavigationBottomBar
import com.jsborbon.reparalo.screens.technician.components.TechnicianCard
import com.jsborbon.reparalo.viewmodels.TechnicianViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TechnicianProfileScreen(
    technicianId: String,
    navController: NavHostController,
) {
    val viewModel: TechnicianViewModel = viewModel()
    val technicianState by viewModel.technician.collectAsState()

    LaunchedEffect(technicianId) {
        viewModel.loadTechnician(technicianId)
    }

    Scaffold(
        bottomBar = {
            NavigationBottomBar(
                selectedIndex = 2,
                navController = navController,
                onTabChange = {},
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (val state = technicianState) {
                is ApiResponse.Loading -> CircularProgressIndicator()

                is ApiResponse.Success -> TechnicianCard(
                    technician = state.data,
                )

                is ApiResponse.Failure -> Text(
                    text = "Error al cargar el técnico: ${state.errorMessage}",
                    color = MaterialTheme.colorScheme.error,
                )

                is ApiResponse.Idle -> Text(
                    text = "Esperando información del técnico...",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}
