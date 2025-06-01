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
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.jsborbon.reparalo.R
import com.jsborbon.reparalo.navigation.Routes
import com.jsborbon.reparalo.ui.theme.Accent
import com.jsborbon.reparalo.ui.theme.PrimaryDark
import com.jsborbon.reparalo.ui.theme.PrimaryLight
import com.jsborbon.reparalo.ui.theme.Secondary
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val startAnimation = remember { mutableStateOf(false) }
    val currentUser = FirebaseAuth.getInstance().currentUser
    val targetRoute = if (currentUser != null) Routes.DASHBOARD else Routes.AUTHENTICATION

    val logoAlpha by animateFloatAsState(
        targetValue = if (startAnimation.value) 1f else 0f,
        animationSpec = tween(1000, easing = LinearOutSlowInEasing),
        label = "logoAlpha",
    )

    val elevation by animateDpAsState(
        targetValue = if (startAnimation.value) 12.dp else 0.dp,
        animationSpec = tween(1200, easing = FastOutSlowInEasing),
        label = "elevation",
    )

    val scale by animateFloatAsState(
        targetValue = if (startAnimation.value) 1f else 0.8f,
        animationSpec = tween(1000, easing = FastOutSlowInEasing),
        label = "scale",
    )

    val infiniteTransition = rememberInfiniteTransition(label = "transition")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "rotation",
    )

    val pulseSize by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "pulse",
    )

    LaunchedEffect(Unit) {
        startAnimation.value = true
        delay(3000)
        navController.navigate(targetRoute) {
            popUpTo(Routes.SPLASH) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(listOf(PrimaryDark, PrimaryLight))),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
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

        Card(
            modifier = Modifier
                .padding(32.dp)
                .alpha(logoAlpha)
                .scale(scale)
                .shadow(elevation, RoundedCornerShape(24.dp), spotColor = Secondary),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(24.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(32.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Brush.radialGradient(listOf(Secondary, PrimaryLight)))
                        .scale(pulseSize),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_reparalo_logo),
                        contentDescription = "Reparalo Logo",
                        tint = Color.White,
                        modifier = Modifier
                            .size(48.dp)
                            .rotate(rotationAngle),
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                AnimatedVisibility(
                    visible = startAnimation.value,
                    enter = fadeIn(tween(1000)) + slideInVertically(initialOffsetY = { -50 }),
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
                    visible = startAnimation.value,
                    enter = fadeIn(tween(1200)) + slideInVertically(initialOffsetY = { 50 }),
                ) {
                    Text(
                        text = "Repara en Casa",
                        color = Color.DarkGray,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

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
