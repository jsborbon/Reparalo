package com.jsborbon.reparalo.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.jsborbon.reparalo.navigation.Routes
import com.jsborbon.reparalo.ui.theme.Accent
import com.jsborbon.reparalo.ui.theme.PrimaryDark
import com.jsborbon.reparalo.ui.theme.PrimaryLight
import com.jsborbon.reparalo.ui.theme.Secondary
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    var startAnimation by remember { mutableStateOf(false) }
    val currentUser = FirebaseAuth.getInstance().currentUser

    // Determinar la ruta de destino después del splash
    val targetRoute = if (currentUser != null) Routes.DASHBOARD else Routes.AUTHENTICATION

    // Animaciones mejoradas
    val logoAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing),
        label = "logoAlpha",
    )

    val elevation by animateDpAsState(
        targetValue = if (startAnimation) 12.dp else 0.dp,
        animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
        label = "elevation",
    )

    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.8f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "scale",
    )

    // Animación infinita para los elementos decorativos
    val infiniteTransition = rememberInfiniteTransition(label = "infiniteTransition")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "rotationAngle",
    )

    val pulseSize by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pulseSize",
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        // Esperar 3 segundos antes de navegar para que se vean las animaciones
        delay(3000)
        navController.navigate(targetRoute) {
            popUpTo(Routes.SPLASH) { inclusive = true }
        }
    }

    // Fondo con gradiente
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        PrimaryDark,
                        PrimaryLight,
                    ),
                ),
            ),
        contentAlignment = Alignment.Center,
    ) {
        // Elementos decorativos de fondo (círculos y líneas)
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Círculos decorativos
            drawCircle(
                color = Color.White.copy(alpha = 0.05f),
                radius = size.width * 0.4f,
                center = Offset(size.width * 0.8f, size.height * 0.2f),
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.05f),
                radius = size.width * 0.3f,
                center = Offset(size.width * 0.2f, size.height * 0.8f),
            )

            // Líneas decorativas
            drawLine(
                color = Secondary.copy(alpha = 0.2f),
                start = Offset(0f, size.height * 0.3f),
                end = Offset(size.width, size.height * 0.7f),
                strokeWidth = 5f,
                cap = StrokeCap.Round,
            )
            drawLine(
                color = Accent.copy(alpha = 0.2f),
                start = Offset(0f, size.height * 0.7f),
                end = Offset(size.width, size.height * 0.3f),
                strokeWidth = 5f,
                cap = StrokeCap.Round,
            )
        }

        // Tarjeta principal con contenido
        Card(
            modifier = Modifier
                .padding(32.dp)
                .alpha(logoAlpha)
                .scale(scale)
                .shadow(elevation = elevation, shape = RoundedCornerShape(24.dp), spotColor = Secondary),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            ),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(32.dp),
            ) {
                // Icono de herramientas con animación
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Brush.radialGradient(listOf(Secondary, PrimaryLight)))
                        .scale(pulseSize),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Default.Build,
                        contentDescription = "Icono de herramientas",
                        tint = Color.White,
                        modifier = Modifier
                            .size(48.dp)
                            .rotate(rotationAngle),
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Logo de la aplicación con animación
                AnimatedVisibility(
                    visible = startAnimation,
                    enter = fadeIn(animationSpec = tween(1000)) +
                        slideInVertically(initialOffsetY = { -50 }, animationSpec = tween(1000)),
                ) {
                    Text(
                        text = "Reparalo",
                        color = PrimaryDark,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                AnimatedVisibility(
                    visible = startAnimation,
                    enter = fadeIn(animationSpec = tween(1200)) +
                        slideInVertically(initialOffsetY = { 50 }, animationSpec = tween(1200)),
                ) {
                    Text(
                        text = "Tu asistente de reparaciones",
                        color = Color.DarkGray,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Indicador de carga personalizado
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(
                        color = Secondary,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 3.dp,
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "Cargando...",
                        color = Color.Gray,
                        fontSize = 16.sp,
                    )
                }
            }
        }
    }
}
