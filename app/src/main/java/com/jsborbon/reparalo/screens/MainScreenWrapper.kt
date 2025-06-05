package com.jsborbon.reparalo.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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

    val selectedIndex = when {
        currentRoute == Routes.DASHBOARD -> 0
        currentRoute == Routes.TUTORIALS -> 1
        currentRoute == Routes.MATERIALS_LIST -> 2
        currentRoute == Routes.FORUM -> 3
        currentRoute == Routes.SETTINGS -> 4
        currentRoute?.contains(Routes.USER_PROFILE) == true -> 5
        else -> 0
    }

    val showBottomNav = currentRoute in bottomNavRoutes ||
        currentRoute?.contains(Routes.USER_PROFILE) == true

    if (showBottomNav) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = when (selectedIndex) {
                                0 -> "Inicio"
                                1 -> "Tutoriales"
                                2 -> "Materiales"
                                3 -> "Foro"
                                4 -> "Ajustes"
                                5 -> "Perfil"
                                else -> "Reparalo"
                            },
                        )
                    },
                )
            },
            bottomBar = {
                NavigationBottomBar(
                    selectedIndex = selectedIndex,
                    navController = navController,
                    onTabChange = { },
                )
            },
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                content()
            }
        }
    } else {
        content()
    }
}
