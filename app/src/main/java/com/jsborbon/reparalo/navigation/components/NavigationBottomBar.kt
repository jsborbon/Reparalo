package com.jsborbon.reparalo.navigation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jsborbon.reparalo.navigation.Routes

@Composable
fun NavigationBottomBar(
    selectedIndex: Int,
    navController: NavHostController,
    onTabChange: (Int) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        NavigationBar(modifier = Modifier.padding(vertical = 4.dp)) {
            BottomBarIcon(
                selected = selectedIndex == 0,
                onClick = {
                    navController.navigateSingleTop(Routes.DASHBOARD)
                    onTabChange(0)
                },
                icon = Icons.Default.Home,
                contentDescription = "Inicio",
            )
            BottomBarIcon(
                selected = selectedIndex == 1,
                onClick = {
                    navController.navigateSingleTop(Routes.TUTORIALS)
                    onTabChange(1)
                },
                icon = Icons.Default.DateRange,
                contentDescription = "Tutoriales",
            )
            BottomBarIcon(
                selected = selectedIndex == 2,
                onClick = {
                    navController.navigateSingleTop(Routes.MATERIALS_LIST)
                    onTabChange(2)
                },
                icon = Icons.Default.Build,
                contentDescription = "Materiales",
            )
            BottomBarIcon(
                selected = selectedIndex == 3,
                onClick = {
                    navController.navigateSingleTop(Routes.FORUM)
                    onTabChange(3)
                },
                icon = Icons.Default.MailOutline,
                contentDescription = "Foro",
                badgeCount = 3,
            )
            BottomBarIcon(
                selected = selectedIndex == 4,
                onClick = {
                    navController.navigateSingleTop(Routes.SETTINGS)
                    onTabChange(4)
                },
                icon = Icons.Default.Settings,
                contentDescription = "Configuración",
            )
            BottomBarIcon(
                selected = selectedIndex == 5,
                onClick = {
                    navController.navigateSingleTop(Routes.USER_PROFILE)
                    onTabChange(5)
                },
                icon = Icons.Default.Person,
                contentDescription = "Perfil",
            )
        }
    }
}

private fun NavHostController.navigateSingleTop(route: String) {
    navigate(route) {
        launchSingleTop = true
        restoreState = true
        popUpTo(graph.startDestinationId) {
            saveState = true
        }
    }
}
