package com.jsborbon.reparalo.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
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
import com.jsborbon.reparalo.ui.theme.PrimaryLight

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

    val showTopBar = remember(currentRoute) {
        currentRoute != Routes.DASHBOARD && showBottomNav
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
            topBar = if (showTopBar) {
                {
                    TopAppBar(
                        title = {
                            Text(
                                text = screenTitle,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.semantics {
                                    contentDescription = "Pantalla actual: $screenTitle"
                                }
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            titleContentColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            } else {
                {
                    TopAppBar(
                        title = {
                            Text(
                                text = "Reparalo",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryLight
                            )
                        },
                        actions = {
                            IconButton(
                                onClick = { navController.navigate(Routes.NOTIFICATIONS) },
                                modifier = Modifier.semantics {
                                    contentDescription = "Ver notificaciones"
                                }
                            ) {
                                Icon(
                                    Icons.Default.Notifications,
                                    contentDescription = "Notificaciones",
                                    tint = PrimaryLight
                                )
                            }
                            IconButton(
                                onClick = { navController.navigate(Routes.USER_PROFILE) },
                                modifier = Modifier.semantics {
                                    contentDescription = "Ver perfil de usuario"
                                }
                            ) {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = "Perfil",
                                    tint = PrimaryLight
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    )
                }
            },
            bottomBar = {
                NavigationBottomBar(
                    selectedIndex = selectedIndex,
                    navController = navController,
                    onTabChange = {},
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
