package com.jsborbon.reparalo.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jsborbon.reparalo.navigation.Routes
import com.jsborbon.reparalo.navigation.components.NavigationBottomBar

private val bottomNavRoutes = listOf(
    Routes.DASHBOARD,
    Routes.TUTORIALS,
    Routes.MATERIALS_LIST,
    Routes.FORUM,
    Routes.SETTINGS,
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenWrapper(
    navController: NavHostController,
    content: @Composable () -> Unit,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val selectedIndex = remember(currentRoute) {
        when {
            currentRoute == Routes.DASHBOARD -> 0
            currentRoute == Routes.TUTORIALS -> 1
            currentRoute == Routes.MATERIALS_LIST -> 2
            currentRoute == Routes.FORUM -> 3
            currentRoute == Routes.SETTINGS -> 4
            currentRoute?.contains(Routes.USER_PROFILE) == true -> 5
            else -> 0
        }
    }

    val showBottomNav = remember(currentRoute) {
        currentRoute in bottomNavRoutes || currentRoute?.contains(Routes.USER_PROFILE) == true
    }

    val screenTitle = remember(selectedIndex) {
        when (selectedIndex) {
            0 -> "Inicio"
            1 -> "Tutoriales"
            2 -> "Materiales"
            3 -> "Foro"
            4 -> "Ajustes"
            5 -> "Perfil"
            else -> "Reparalo"
        }
    }

    if (showBottomNav) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        AnimatedContent(
                            targetState = screenTitle,
                            transitionSpec = {
                                fadeIn(animationSpec = tween(200)) togetherWith
                                    fadeOut(animationSpec = tween(150))
                            },
                            label = "topBarTitle"
                        ) { title ->
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.semantics {
                                    contentDescription = "Pantalla actual: $title"
                                }
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            },
            bottomBar = {
                NavigationBottomBar(
                    selectedIndex = selectedIndex,
                    navController = navController,
                    onTabChange = { /* Navigation handled by NavController */ },
                )
            },
            containerColor = MaterialTheme.colorScheme.background,
            modifier = Modifier.semantics {
                contentDescription = "Pantalla principal con navegación"
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .semantics {
                        contentDescription = "Contenido de $screenTitle"
                    }
            ) {
                content()
            }
        }
    } else {
        Box(
            modifier = Modifier.semantics {
                contentDescription = "Pantalla sin navegación"
            }
        ) {
            content()
        }
    }
}
