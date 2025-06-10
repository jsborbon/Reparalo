
package com.jsborbon.reparalo.screens.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jsborbon.reparalo.navigation.Routes
import com.jsborbon.reparalo.screens.dashboard.components.CategorySection
import com.jsborbon.reparalo.screens.dashboard.components.HighlightedTechnicianCard
import com.jsborbon.reparalo.screens.dashboard.components.HighlightedTutorialCard
import com.jsborbon.reparalo.screens.dashboard.components.WelcomeSection
import com.jsborbon.reparalo.ui.theme.PrimaryLight
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
                title = {
                    Text(
                        text = "Reparalo",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryLight
                    )
                },
                actions = {
                    IconButton(
                        onClick = { navController.navigate(Routes.NOTIFICATIONS) },
                        modifier = Modifier
                            .size(48.dp)
                            .semantics { contentDescription = "Ver notificaciones" }
                    ) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = "Notificaciones",
                            tint = PrimaryLight
                        )
                    }
                    IconButton(
                        onClick = { navController.navigate(Routes.USER_PROFILE) },
                        modifier = Modifier
                            .size(48.dp)
                            .semantics { contentDescription = "Ver perfil de usuario" }
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Perfil",
                            tint = PrimaryLight
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                )
            )
        },
    ) { innerPadding ->
        EnhancedDashboardContent(innerPadding = innerPadding, navController = navController)
    }
}

@Composable
fun EnhancedDashboardContent(innerPadding: PaddingValues, navController: NavController) {
    val screenKey = navController.currentBackStackEntryAsState().value?.destination?.route

    key(screenKey) {
        val visibleState = remember { mutableStateOf(false) }
        val contentAnimationStates = remember {
            List(4) { mutableStateOf(false) }
        }

        LaunchedEffect(Unit) {
            visibleState.value = true
            // Staggered content animation
            contentAnimationStates.forEachIndexed { index, state ->
                kotlinx.coroutines.delay(index * 150L)
                state.value = true
            }
        }

        AnimatedVisibility(
            visible = visibleState.value,
            enter = slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(500)
            ) + fadeIn(animationSpec = tween(500)),
            exit = slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300)),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                // Welcome Section with animation
                AnimatedVisibility(
                    visible = contentAnimationStates[0].value,
                    enter = slideInVertically(
                        initialOffsetY = { -it/2 },
                        animationSpec = tween(600)
                    ) + fadeIn(animationSpec = tween(600))
                ) {
                    WelcomeSection()
                }

                // Category Section with animation
                AnimatedVisibility(
                    visible = contentAnimationStates[1].value,
                    enter = slideInVertically(
                        initialOffsetY = { it/3 },
                        animationSpec = tween(600)
                    ) + fadeIn(animationSpec = tween(600))
                ) {
                    CategorySection(navController)
                }

                // Tutorial Card with animation
                AnimatedVisibility(
                    visible = contentAnimationStates[2].value,
                    enter = slideInVertically(
                        initialOffsetY = { it/3 },
                        animationSpec = tween(600)
                    ) + fadeIn(animationSpec = tween(600))
                ) {
                    HighlightedTutorialCard(navController)
                }

                // Technician Card with animation
                AnimatedVisibility(
                    visible = contentAnimationStates[3].value,
                    enter = slideInVertically(
                        initialOffsetY = { it/3 },
                        animationSpec = tween(600)
                    ) + fadeIn(animationSpec = tween(600))
                ) {
                    HighlightedTechnicianCard(navController)
                }
            }
        }
    }
}
