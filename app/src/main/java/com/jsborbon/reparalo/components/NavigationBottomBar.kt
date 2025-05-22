package com.jsborbon.reparalo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.jsborbon.reparalo.navigation.Routes
import com.jsborbon.reparalo.screens.dashboard.DashboardScreen
import com.jsborbon.reparalo.screens.forum.ForumScreen
import com.jsborbon.reparalo.screens.material.MaterialsListScreen
import com.jsborbon.reparalo.screens.profile.UserProfileScreen
import com.jsborbon.reparalo.screens.settings.SettingsScreen
import com.jsborbon.reparalo.screens.tutorial.TutorialsScreen

@Composable
fun NavigationBottomBar(
    selectedIndex: Int,
    navController: NavController,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        NavigationBar(modifier = Modifier.padding(vertical = 4.dp)) {
            BottomBarIcon(
                selected = selectedIndex == 0,
                onClick = { navController.navigateAndPopUpTo(Routes.DASHBOARD, Routes.DASHBOARD) },
                icon = Icons.Default.Home,
                contentDescription = "Inicio",
            )
            BottomBarIcon(
                selected = selectedIndex == 1,
                onClick = { navController.navigateAndPopUpTo(Routes.TUTORIALS, Routes.DASHBOARD) },
                icon = Icons.Default.DateRange,
                contentDescription = "Tutoriales",
            )
            BottomBarIcon(
                selected = selectedIndex == 2,
                onClick = { navController.navigateAndPopUpTo(Routes.MATERIALS_LIST, Routes.DASHBOARD) },
                icon = Icons.Default.Build,
                contentDescription = "Materiales",
            )
            BottomBarIcon(
                selected = selectedIndex == 3,
                onClick = { navController.navigateAndPopUpTo(Routes.FORUM, Routes.DASHBOARD) },
                icon = Icons.Default.MailOutline,
                contentDescription = "Foro",
                badgeCount = 3,
            )
            BottomBarIcon(
                selected = selectedIndex == 4,
                onClick = { navController.navigateAndPopUpTo(Routes.SETTINGS, Routes.DASHBOARD) },
                icon = Icons.Default.Settings,
                contentDescription = "Configuración",
            )
            BottomBarIcon(
                selected = selectedIndex == 5,
                onClick = { navController.navigateAndPopUpTo(Routes.USER_PROFILE, Routes.DASHBOARD) },
                icon = Icons.Default.Person,
                contentDescription = "Perfil",
            )
        }
    }
}

@Composable
fun BottomBarIcon(
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String,
    badgeCount: Int = 0,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clickable { onClick() },
    ) {
        Box {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                modifier = Modifier.size(26.dp),
                tint = if (selected) {
                    MaterialTheme.colorScheme.secondary
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                },
            )
            if (badgeCount > 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 6.dp, y = (-4).dp)
                        .size(14.dp)
                        .background(
                            color = MaterialTheme.colorScheme.error,
                            shape = MaterialTheme.shapes.extraSmall,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = badgeCount.toString(),
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                    )
                }
            }
        }
        Text(
            text = contentDescription,
            style = MaterialTheme.typography.labelSmall,
            color = if (selected) {
                MaterialTheme.colorScheme.secondary
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            },
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}

@Composable
fun BottomNavHost(
    navController: NavHostController,
    onTabChange: (Int) -> Unit,
) {
    androidx.navigation.compose.NavHost(
        navController = navController,
        startDestination = Routes.DASHBOARD,
    ) {
        composable(Routes.DASHBOARD) {
            onTabChange(0)
            DashboardScreen(navController = navController)
        }
        composable(Routes.TUTORIALS) {
            onTabChange(1)
            TutorialsScreen(navController = navController)
        }
        composable(Routes.MATERIALS_LIST) {
            onTabChange(2)
            MaterialsListScreen(navController = navController)
        }
        composable(Routes.FORUM) {
            onTabChange(3)
            ForumScreen(navController = navController)
        }
        composable(Routes.SETTINGS) {
            onTabChange(4)
            SettingsScreen(navController = navController)
        }
        composable(Routes.USER_PROFILE) {
            onTabChange(5)
            UserProfileScreen(navController = navController)
        }
    }
}

private fun NavController.navigateAndPopUpTo(
    route: String,
    popUpToRoute: String,
    inclusive: Boolean = false,
) {
    navigate(route) {
        popUpTo(popUpToRoute) { this.inclusive = inclusive }
        launchSingleTop = true
    }
}
