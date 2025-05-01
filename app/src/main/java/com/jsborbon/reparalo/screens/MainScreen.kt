package com.jsborbon.reparalo.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jsborbon.reparalo.components.NavigationBottomBar

object BottomNavRoutes {
    const val DASHBOARD = "dashboard"
    const val TUTORIALES = "tutoriales"
    const val MATERIALES = "materiales"
    const val FORO = "foro"
    const val AJUSTES = "ajustes"
    const val PERFIL = "perfil"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    var selectedIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (selectedIndex) {
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
            )
        },
    ) { innerPadding ->
        BottomNavHost(innerPadding, navController, onTabChange = { selectedIndex = it })
    }
}

@Composable
fun BottomNavHost(
    innerPadding: PaddingValues,
    navController: NavHostController,
    onTabChange: (Int) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavRoutes.DASHBOARD,
    ) {
        composable(BottomNavRoutes.DASHBOARD) {
            onTabChange(0)
            DashboardScreen(navController)
        }
        composable(BottomNavRoutes.TUTORIALES) {
            onTabChange(1)
            TutorialesScreen(navController)
        }
        composable(BottomNavRoutes.MATERIALES) {
            onTabChange(2)
            Text("Materiales (por implementar)")
        }
        composable(BottomNavRoutes.FORO) {
            onTabChange(3)
            Text("Foro (por implementar)")
        }
        composable(BottomNavRoutes.AJUSTES) {
            onTabChange(4)
            Text("Ajustes (por implementar)")
        }
        composable(BottomNavRoutes.PERFIL) {
            onTabChange(5)
            Text("Perfil (por implementar)")
        }
    }
}
