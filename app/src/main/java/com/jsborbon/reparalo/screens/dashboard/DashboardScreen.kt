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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jsborbon.reparalo.screens.dashboard.components.CategorySection
import com.jsborbon.reparalo.screens.dashboard.components.HighlightedTechnicianCard
import com.jsborbon.reparalo.screens.dashboard.components.HighlightedTutorialCard
import com.jsborbon.reparalo.screens.dashboard.components.WelcomeSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
) {

    Scaffold { innerPadding ->
        DashboardContent(innerPadding = innerPadding, navController = navController)
    }
}

@Composable
fun DashboardContent(innerPadding: PaddingValues, navController: NavController) {
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
