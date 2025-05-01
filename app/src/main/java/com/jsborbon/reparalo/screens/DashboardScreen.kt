package com.jsborbon.reparalo.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.firebase.auth.FirebaseAuth
import com.jsborbon.reparalo.components.PopoverMenu
import com.jsborbon.reparalo.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController) {
    val currentUser = FirebaseAuth.getInstance().currentUser

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reparalo") },
                actions = {
                    IconButton(onClick = { navController.navigate(Routes.NOTIFICATIONS) }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notificaciones")
                    }
                    IconButton(onClick = { navController.navigate(Routes.USER_PROFILE) }) {
                        Icon(Icons.Default.Person, contentDescription = "Perfil")
                    }
                    PopoverMenu()
                },
            )
        },
    ) { innerPadding ->
        DashboardContent(innerPadding, navController)
    }
}

@Composable
fun DashboardContent(
    innerPadding: PaddingValues,
    navController: NavController,
) {
    // Usa el back stack como clave para reiniciar la animación al volver
    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val screenKey = currentBackStackEntry?.destination?.route

    key(screenKey) {
        var visibleState = remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            visibleState.value = true
        }

        AnimatedVisibility(
            visible = visibleState.value,
            enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
            exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut(),
        ) {
            Column(
                modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                // Sección de bienvenida
                Text(
                    text = "Bienvenido a Reparalo",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )

                Text(
                    text = "¿Qué deseas hacer hoy?",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Sección de categorías principales
                Row(modifier = Modifier.fillMaxWidth()) {
                    CategoryCard(
                        title = "Tutoriales",
                        icon = Icons.Default.Star,
                        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.weight(1f),
                        onClick = { navController.navigate(Routes.TUTORIALS) },
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    CategoryCard(
                        title = "Materiales",
                        icon = Icons.Default.Build,
                        backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                        modifier = Modifier.weight(1f),
                        onClick = { navController.navigate(Routes.MATERIALS_LIST) },
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    CategoryCard(
                        title = "Profesionales",
                        icon = Icons.Default.Person,
                        backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
                        modifier = Modifier.weight(1f),
                        onClick = { navController.navigate(Routes.PROFESSIONAL_CONNECTION) },
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    CategoryCard(
                        title = "Foro",
                        icon = Icons.Default.Info,
                        backgroundColor = Color(0xFFE1F5FE),
                        modifier = Modifier.weight(1f),
                        onClick = { navController.navigate(Routes.FORUM) },
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Sección de tutoriales destacados
                Text(
                    text = "Tutoriales Destacados",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp),
                )

                // Aquí se mostrarían los tutoriales destacados
                // Por ahora, mostraremos un placeholder
                Card(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { navController.navigate(Routes.TUTORIALS) },
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Cómo reparar una fuga en el lavamanos",
                            fontWeight = FontWeight.Bold,
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Aprende a solucionar problemas comunes de plomería en tu hogar",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFC107),
                                modifier = Modifier.size(16.dp),
                            )

                            Text(
                                text = "4.8",
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 4.dp),
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                            )

                            Text(
                                text = "15 min",
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 4.dp),
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Text(
                                text = "Fácil",
                                fontSize = 12.sp,
                                color = Color(0xFF4CAF50),
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Sección de profesionales destacados
                Text(
                    text = "Profesionales Destacados",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp),
                )

                // Aquí se mostrarían los profesionales destacados
                // Por ahora, mostraremos un placeholder
                Card(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { navController.navigate(Routes.PROFESSIONAL_CONNECTION) },
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier =
                            Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(25.dp))
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "JR",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Juan Rodríguez",
                                fontWeight = FontWeight.Bold,
                            )

                            Text(
                                text = "Electricista • 4.9 ★",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            )
                        }

                        Button(
                            onClick = { /* Contactar profesional */ },
                            modifier = Modifier.padding(start = 8.dp),
                        ) {
                            Text("Contactar")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryCard(
    title: String,
    icon: ImageVector,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Card(
        modifier =
        modifier
            .height(120.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
    ) {
        Column(
            modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
