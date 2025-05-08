package com.jsborbon.reparalo.screens

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
import com.jsborbon.reparalo.components.BottomNavHost
import com.jsborbon.reparalo.components.NavigationBottomBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    var selectedIndex by remember { mutableIntStateOf(0) }

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
                        }
                    )
                }
            )
        },
        bottomBar = {
            NavigationBottomBar(
                selectedIndex = selectedIndex,
                navController = navController
            )
        }
    ) { innerPadding ->
        BottomNavHost(
            navController = navController,
            onTabChange = { selectedIndex = it }
        )
    }
}
