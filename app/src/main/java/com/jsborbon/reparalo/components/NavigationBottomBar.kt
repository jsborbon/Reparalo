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
import com.jsborbon.reparalo.navigation.Routes

@Composable
fun NavigationBottomBar(
    selectedIndex: Int,
    navController: NavController,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        NavigationBar(
            modifier = Modifier.padding(vertical = 4.dp),
        ) {
            BottomBarIcon(
                selected = selectedIndex == 0,
                onClick = {
                    if (selectedIndex != 0) {
                        navController.navigate(Routes.DASHBOARD) {
                            popUpTo(Routes.DASHBOARD) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                },
                icon = Icons.Default.Home,
                contentDescription = "Inicio",
            )
            BottomBarIcon(
                selected = selectedIndex == 1,
                onClick = {
                    if (selectedIndex != 1) {
                        navController.navigate(Routes.TUTORIALS) {
                            popUpTo(Routes.DASHBOARD) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                },
                icon = Icons.Default.DateRange,
                contentDescription = "Calendario",
            )
            BottomBarIcon(
                selected = selectedIndex == 2,
                onClick = {
                    if (selectedIndex != 2) {
                        navController.navigate(Routes.MATERIALS_LIST) {
                            popUpTo(Routes.DASHBOARD) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                },
                icon = Icons.Default.Build,
                contentDescription = "Reparaciones",
            )
            BottomBarIcon(
                selected = selectedIndex == 3,
                onClick = {
                    if (selectedIndex != 3) {
                        navController.navigate(Routes.FORUM) {
                            popUpTo(Routes.DASHBOARD) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                },
                icon = Icons.Default.MailOutline,
                contentDescription = "Mensajes",
                badgeCount = 3,
            )
            BottomBarIcon(
                selected = selectedIndex == 4,
                onClick = {
                    if (selectedIndex != 4) {
                        navController.navigate(Routes.SETTINGS) {
                            popUpTo(Routes.DASHBOARD) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                },
                icon = Icons.Default.Settings,
                contentDescription = "Ajustes",
            )
            BottomBarIcon(
                selected = selectedIndex == 5,
                onClick = {
                    if (selectedIndex != 5) {
                        navController.navigate(Routes.USER_PROFILE) {
                            popUpTo(Routes.DASHBOARD) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                },
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
                    MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.6f,
                    )
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
                MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.6f,
                )
            },
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}
