package com.jsborbon.reparalo.navigation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jsborbon.reparalo.R
import com.jsborbon.reparalo.models.NavigationItem
import com.jsborbon.reparalo.navigation.Routes

@Composable
fun NavigationBottomBar(
    selectedIndex: Int,
    navController: NavHostController,
    onTabChange: (Int) -> Unit,
    isVisible: Boolean = true,
    badgeCounts: Map<Int, Int> = emptyMap(),
) {

    val containerWidth = LocalWindowInfo.current.containerSize.width
    val isCompactScreen = containerWidth < 600

    val playLessonIcon = painterResource(id = R.drawable.outline_play_lesson)
    val forumIcon = painterResource(id = R.drawable.outline_forum)

    val navigationItems = listOf(
        NavigationItem(
            icon = Icons.Default.Home,
            label = "Inicio",
            route = Routes.DASHBOARD,
            index = 0
        ),
        NavigationItem(
            iconPainter = playLessonIcon,
            label = "Tutoriales",
            route = Routes.TUTORIALS,
            index = 1
        ),
        NavigationItem(
            icon = Icons.Default.Build,
            label = "Materiales",
            route = Routes.MATERIALS_LIST,
            index = 2
        ),
        NavigationItem(
            iconPainter = forumIcon,
            label = "Foro",
            route = Routes.FORUM,
            index = 3
        ),
        NavigationItem(
            icon = Icons.Default.Settings,
            label = if (isCompactScreen) "Config" else "Configuración",
            route = Routes.SETTINGS,
            index = 4
        ),
        NavigationItem(
            icon = Icons.Default.Person,
            label = "Perfil",
            route = Routes.USER_PROFILE,
            index = 5
        )
    )

    val containerAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(300),
        label = "containerAlpha"
    )

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(containerAlpha)
                .semantics {
                    contentDescription = "Barra de navegación principal con ${navigationItems.size} opciones"
                }
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = Color.Transparent,
                shadowElevation = 16.dp
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                                    MaterialTheme.colorScheme.surface.copy(alpha = 0.98f),
                                    MaterialTheme.colorScheme.surface
                                )
                            )
                        )
                        .blur(radius = 0.5.dp)
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.98f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp, vertical = 8.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth(0.3f)
                            .align(Alignment.Center),
                        shape = RoundedCornerShape(2.dp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                    ) {
                        Box(modifier = Modifier.height(4.dp))
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = if (isCompactScreen) 8.dp else 16.dp,
                            vertical = 12.dp
                        )
                        .windowInsetsPadding(WindowInsets.navigationBars),
                    horizontalArrangement = if (isCompactScreen)
                        Arrangement.SpaceEvenly
                    else
                        Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    navigationItems.forEach { item ->
                        BottomBarIcon(
                            selected = selectedIndex == item.index,
                            onClick = {
                                if (selectedIndex != item.index) {
                                    navController.navigateSingleTop(item.route)
                                    onTabChange(item.index)
                                }
                            },
                            icon = item.icon,
                            iconPainter = item.iconPainter,
                            contentDescription = item.label,
                            badgeCount = badgeCounts[item.index] ?: 0,
                            isEnabled = true
                        )
                    }
                }
            }
        }
    }
}

private fun NavHostController.navigateSingleTop(route: String) {
    if (currentDestination?.route == route) return
    navigate(route) {
        launchSingleTop = true
        restoreState = true
        popUpTo(graph.startDestinationId) {
            saveState = true
        }
    }
}
