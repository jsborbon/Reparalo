package com.jsborbon.reparalo.screens.splashScreen

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.jsborbon.reparalo.navigation.Routes
import com.jsborbon.reparalo.ui.components.LoadingIndicator
import com.jsborbon.reparalo.ui.theme.Accent
import com.jsborbon.reparalo.ui.theme.PrimaryDark
import com.jsborbon.reparalo.ui.theme.PrimaryLight
import com.jsborbon.reparalo.ui.theme.RepairYellow
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val startAnimation = remember { mutableStateOf(false) }
    val hasNavigated = remember { mutableStateOf(false) }
    val currentUser = remember { FirebaseAuth.getInstance().currentUser }
    val targetRoute = if (currentUser != null) Routes.DASHBOARD else Routes.AUTHENTICATION

    val logoAlpha by animateFloatAsState(
        targetValue = if (startAnimation.value) 1f else 0f,
        animationSpec = tween(1200, easing = FastOutSlowInEasing, delayMillis = 300),
        label = "logoAlpha",
    )

    val cardElevation by animateDpAsState(
        targetValue = if (startAnimation.value) 16.dp else 0.dp,
        animationSpec = tween(1400, easing = FastOutSlowInEasing, delayMillis = 500),
        label = "cardElevation",
    )

    val logoScale by animateFloatAsState(
        targetValue = if (startAnimation.value) 1f else 0.7f,
        animationSpec = tween(1200, easing = FastOutSlowInEasing, delayMillis = 200),
        label = "logoScale",
    )

    val infiniteTransition = rememberInfiniteTransition(label = "infiniteTransition")

    val iconRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "iconRotation",
    )

    val pulseAnimation by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pulseAnimation",
    )

    val backgroundShimmer by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "backgroundShimmer",
    )

    LaunchedEffect(Unit) {
        if (!hasNavigated.value) {
            hasNavigated.value = true
            startAnimation.value = true
            delay(3500)
            navController.navigate(targetRoute) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(PrimaryDark, PrimaryLight, Accent.copy(alpha = 0.1f)),
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                )
            )
            .semantics {
                contentDescription = "Pantalla de inicio de Reparalo, cargando aplicación"
            },
        contentAlignment = Alignment.Center,
    ) {
        SplashBackground(shimmerProgress = backgroundShimmer)

        Card(
            modifier = Modifier
                .padding(horizontal = 40.dp, vertical = 32.dp)
                .graphicsLayer {
                    alpha = logoAlpha
                    scaleX = logoScale
                    scaleY = logoScale
                }
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                RepairYellow.copy(alpha = 0.1f),
                                Color.Transparent
                            ),
                            radius = size.maxDimension * 0.8f
                        ),
                        radius = size.maxDimension * 0.6f,
                        center = center
                    )
                },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
            ),
            shape = RoundedCornerShape(28.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = cardElevation),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 40.dp),
            ) {
                LogoContainer(
                    pulseScale = pulseAnimation,
                    rotationAngle = iconRotation,
                )

                Spacer(modifier = Modifier.height(28.dp))

                AnimatedTextContent(isVisible = startAnimation.value)

                Spacer(modifier = Modifier.height(36.dp))

                LoadingIndicator()
            }
        }
    }
}
