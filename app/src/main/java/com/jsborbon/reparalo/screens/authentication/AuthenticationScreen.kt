package com.jsborbon.reparalo.screens.authentication

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jsborbon.reparalo.R
import com.jsborbon.reparalo.screens.authentication.components.LoginForm
import com.jsborbon.reparalo.screens.authentication.components.SignUpForm
import com.jsborbon.reparalo.viewmodels.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun AuthenticationScreen(
    navController: NavController,
) {
    // ViewModel and state management
    val authViewModel: AuthViewModel = viewModel()
    var isLoginSelected by remember { mutableStateOf(true) }
    var logoAnimationTriggered by remember { mutableStateOf(false) }
    var currentTabIndex by remember { mutableIntStateOf(0) }

    // Configuration for responsive design
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val isCompactScreen = screenHeight < 700.dp

    // Enhanced logo animations with spring physics
    val logoScaleAnimation by animateFloatAsState(
        targetValue = if (logoAnimationTriggered) 1f else 0.3f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "logoScale",
    )

    val logoAlphaAnimation by animateFloatAsState(
        targetValue = if (logoAnimationTriggered) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "logoAlpha",
    )

    // Trigger logo animation on screen load
    LaunchedEffect(Unit) {
        delay(150) // Slight delay for better visual impact
        logoAnimationTriggered = true
    }

    // Update tab index when login/signup selection changes
    LaunchedEffect(isLoginSelected) {
        currentTabIndex = if (isLoginSelected) 0 else 1
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.03f),
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                    ),
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                ),
            )
            .semantics {
                contentDescription = "Pantalla de autenticación de Reparalo"
            },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = if (isCompactScreen) 16.dp else 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Responsive top spacing
            Spacer(modifier = Modifier.height(if (isCompactScreen) 24.dp else 48.dp))

            // Enhanced animated logo with better visual hierarchy
            AnimatedContent(
                targetState = logoAnimationTriggered,
                label = "logoAnimationContent",
                transitionSpec = {
                    fadeIn(
                        animationSpec = tween(durationMillis = 800)
                    ) togetherWith fadeOut(
                        animationSpec = tween(durationMillis = 400)
                    )
                }
            ) { animationStarted ->
                if (animationStarted) {
                    Box(
                        modifier = Modifier
                            .size(if (isCompactScreen) 100.dp else 120.dp)
                            .scale(logoScaleAnimation)
                            .alpha(logoAlphaAnimation)
                            .semantics {
                                contentDescription = "Logo de Reparalo"
                            },
                    ) {
                        Card(
                            modifier = Modifier.fillMaxSize(),
                            shape = RoundedCornerShape(28.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                            ),
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_reparalo_logo),
                                    contentDescription = "Icono de Reparalo",
                                    modifier = Modifier.size(if (isCompactScreen) 60.dp else 80.dp),
                                    contentScale = ContentScale.Fit,
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(if (isCompactScreen) 16.dp else 24.dp))

            // Enhanced brand title with better typography
            Text(
                text = "Reparalo",
                fontSize = if (isCompactScreen) 36.sp else 42.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.semantics {
                    contentDescription = "Reparalo, aplicación de reparaciones"
                }
            )

            // Enhanced subtitle with better accessibility
            Text(
                text = "Tu asistente inteligente de reparaciones",
                fontSize = if (isCompactScreen) 14.sp else 16.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(horizontal = if (isCompactScreen) 24.dp else 32.dp)
                    .semantics {
                        contentDescription = "Descripción: asistente inteligente para reparaciones domésticas"
                    },
            )

            Spacer(modifier = Modifier.height(if (isCompactScreen) 28.dp else 40.dp))

            // Enhanced main content card with improved elevation and styling
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics {
                        contentDescription = "Formulario de autenticación"
                    },
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            ) {
                Column(
                    modifier = Modifier.padding(
                        horizontal = if (isCompactScreen) 20.dp else 24.dp,
                        vertical = if (isCompactScreen) 20.dp else 24.dp
                    )
                ) {
                    // Enhanced tab selection with improved visual feedback
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics {
                                contentDescription = "Selección entre inicio de sesión y registro"
                            },
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        shadowElevation = 2.dp
                    ) {
                        TabRow(
                            selectedTabIndex = currentTabIndex,
                            containerColor = Color.Transparent,
                            indicator = { tabPositions ->
                                if (tabPositions.isNotEmpty() && currentTabIndex < tabPositions.size) {
                                    Surface(
                                        modifier = Modifier
                                            .tabIndicatorOffset(tabPositions[currentTabIndex])
                                            .height(52.dp)
                                            .padding(6.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        color = MaterialTheme.colorScheme.primary,
                                        shadowElevation = 6.dp
                                    ) {}
                                }
                            },
                            divider = {},
                        ) {
                            // Login tab with enhanced accessibility
                            Tab(
                                selected = isLoginSelected,
                                onClick = {
                                    isLoginSelected = true
                                    currentTabIndex = 0
                                },
                                modifier = Modifier
                                    .height(52.dp)
                                    .semantics {
                                        contentDescription = if (isLoginSelected)
                                            "Pestaña de inicio de sesión seleccionada"
                                        else
                                            "Cambiar a inicio de sesión"
                                    },
                                text = {
                                    Text(
                                        "Iniciar Sesión",
                                        fontWeight = if (isLoginSelected) FontWeight.Bold else FontWeight.SemiBold,
                                        fontSize = if (isCompactScreen) 14.sp else 15.sp,
                                        color = if (isLoginSelected) {
                                            MaterialTheme.colorScheme.onPrimary
                                        } else {
                                            MaterialTheme.colorScheme.onSurface
                                        },
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                },
                            )

                            // Sign up tab with enhanced accessibility
                            Tab(
                                selected = !isLoginSelected,
                                onClick = {
                                    isLoginSelected = false
                                    currentTabIndex = 1
                                },
                                modifier = Modifier
                                    .height(52.dp)
                                    .semantics {
                                        contentDescription = if (!isLoginSelected)
                                            "Pestaña de registro seleccionada"
                                        else
                                            "Cambiar a registro"
                                    },
                                text = {
                                    Text(
                                        "Registrarse",
                                        fontWeight = if (!isLoginSelected) FontWeight.Bold else FontWeight.SemiBold,
                                        fontSize = if (isCompactScreen) 14.sp else 15.sp,
                                        color = if (!isLoginSelected) {
                                            MaterialTheme.colorScheme.onPrimary
                                        } else {
                                            MaterialTheme.colorScheme.onSurface
                                        },
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                },
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(if (isCompactScreen) 24.dp else 32.dp))

                    // Enhanced form content transition with improved animations
                    AnimatedContent(
                        targetState = isLoginSelected,
                        transitionSpec = {
                            // Smooth horizontal slide with spring animation
                            slideInHorizontally(
                                initialOffsetX = { screenWidth ->
                                    if (targetState) -screenWidth / 2 else screenWidth / 2
                                },
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioNoBouncy,
                                    stiffness = Spring.StiffnessMediumLow
                                ),
                            ) + fadeIn(
                                animationSpec = tween(durationMillis = 400)
                            ) togetherWith slideOutHorizontally(
                                targetOffsetX = { screenWidth ->
                                    if (targetState) screenWidth / 2 else -screenWidth / 2
                                },
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioNoBouncy,
                                    stiffness = Spring.StiffnessMediumLow
                                ),
                            ) + fadeOut(
                                animationSpec = tween(durationMillis = 300)
                            )
                        },
                        label = "authenticationFormTransition",
                    ) { showLoginForm ->
                        if (showLoginForm) {
                            LoginForm(
                                navController = navController,
                                viewModel = authViewModel
                            )
                        } else {
                            SignUpForm(
                                navController = navController,
                                viewModel = authViewModel
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(if (isCompactScreen) 20.dp else 32.dp))

            // Enhanced footer with better visual hierarchy and accessibility
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics {
                        contentDescription = "Mensaje promocional de Reparalo"
                    },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "🔧",
                        fontSize = if (isCompactScreen) 18.sp else 20.sp,
                        modifier = Modifier.semantics {
                            contentDescription = "Icono de herramienta"
                        }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Soluciones rápidas y confiables",
                        fontSize = if (isCompactScreen) 13.sp else 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }

            // Bottom spacing for better visual balance
            Spacer(modifier = Modifier.height(if (isCompactScreen) 16.dp else 24.dp))
        }
    }
}
