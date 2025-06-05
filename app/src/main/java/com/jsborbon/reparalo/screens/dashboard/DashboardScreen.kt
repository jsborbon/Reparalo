package com.jsborbon.reparalo.screens.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jsborbon.reparalo.navigation.Routes
import com.jsborbon.reparalo.screens.dashboard.components.CategorySection
import com.jsborbon.reparalo.screens.dashboard.components.HighlightedTechnicianCard
import com.jsborbon.reparalo.screens.dashboard.components.HighlightedTutorialCard
import com.jsborbon.reparalo.screens.dashboard.components.WelcomeSection
import com.jsborbon.reparalo.viewmodels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
) {
    val viewModel: AuthViewModel = viewModel()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reparalo") },
                actions = {
                    IconButton(onClick = { navController.navigate(Routes.NOTIFICATIONS) }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notificaciones")
                    }
                    IconButton(onClick = { navController.navigate(Routes.USER_PROFILE) }) {
                        Icon(Icons.Default.Person, contentDescription = "Perfil")
                    }
                },
            )
        },
    ) { innerPadding ->
        DashboardContent(innerPadding = innerPadding, navController = navController)
    }
}

@Composable
fun DashboardContent(innerPadding: PaddingValues, navController: NavController) {
    val screenKey = navController.currentBackStackEntryAsState().value?.destination?.route
    key(screenKey) {
        val visibleState = remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            visibleState.value = true
        }

        AnimatedVisibility(
            visible = visibleState.value,
            enter = slideInHorizontally { it } + fadeIn(),
            exit = slideOutHorizontally { -it } + fadeOut(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                WelcomeSection()
                CategorySection(navController)
                HighlightedTutorialCard(navController)
                HighlightedTechnicianCard(navController)
            }
        }
    }
}
