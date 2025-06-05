package com.jsborbon.reparalo.screens.authentication

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.ui.res.painterResource
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
    val viewModel: AuthViewModel = viewModel()
    var isLogin by remember { mutableStateOf(true) }
    var logoAnimationStarted by remember { mutableStateOf(false) }

    val logoScale by animateFloatAsState(
        targetValue = if (logoAnimationStarted) 1f else 0.5f,
        animationSpec = tween(800),
        label = "logoScale",
    )

    val logoAlpha by animateFloatAsState(
        targetValue = if (logoAnimationStarted) 1f else 0f,
        animationSpec = tween(600),
        label = "logoAlpha",
    )

    LaunchedEffect(Unit) {
        delay(200)
        logoAnimationStarted = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                    ),
                ),
            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Logo animado con imagen
            AnimatedContent(
                targetState = logoAnimationStarted,
                label = "logoContent",
            ) { started ->
                if (started) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .scale(logoScale)
                            .alpha(logoAlpha),
                    ) {
                        Card(
                            modifier = Modifier.fillMaxSize(),
                            shape = RoundedCornerShape(24.dp),
                            elevation = CardDefaults.cardElevation(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                            ),
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_reparalo_logo),
                                    contentDescription = "Reparalo Logo",
                                    modifier = Modifier.size(80.dp),
                                    contentScale = ContentScale.Fit,
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Reparalo",
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
            )

            Text(
                text = "Tu asistente inteligente de reparaciones",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp),
            )

            Spacer(modifier = Modifier.height(40.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    ) {
                        TabRow(
                            selectedTabIndex = if (isLogin) 0 else 1,
                            containerColor = Color.Transparent,
                            indicator = { tabPositions ->
                                if (tabPositions.isNotEmpty()) {
                                    Surface(
                                        modifier = Modifier
                                            .tabIndicatorOffset(tabPositions[if (isLogin) 0 else 1])
                                            .height(48.dp)
                                            .padding(4.dp),
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.primary,
                                    ) {}
                                }
                            },
                            divider = {},
                        ) {
                            Tab(
                                selected = isLogin,
                                onClick = { isLogin = true },
                                modifier = Modifier.height(48.dp),
                                text = {
                                    Text(
                                        "Iniciar Sesión",
                                        fontWeight = if (isLogin) FontWeight.SemiBold else FontWeight.Normal,
                                        color = if (isLogin) {
                                            MaterialTheme.colorScheme.onPrimary
                                        } else {
                                            MaterialTheme.colorScheme.onSurface
                                        },
                                    )
                                },
                            )
                            Tab(
                                selected = !isLogin,
                                onClick = { isLogin = false },
                                modifier = Modifier.height(48.dp),
                                text = {
                                    Text(
                                        "Registrarse",
                                        fontWeight = if (!isLogin) FontWeight.SemiBold else FontWeight.Normal,
                                        color = if (!isLogin) {
                                            MaterialTheme.colorScheme.onPrimary
                                        } else {
                                            MaterialTheme.colorScheme.onSurface
                                        },
                                    )
                                },
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    AnimatedContent(
                        targetState = isLogin,
                        transitionSpec = {
                            slideInHorizontally(
                                initialOffsetX = { width -> if (targetState) -width else width },
                                animationSpec = tween(300),
                            ) + fadeIn(animationSpec = tween(300)) togetherWith
                                slideOutHorizontally(
                                    targetOffsetX = { width -> if (targetState) width else -width },
                                    animationSpec = tween(300),
                                ) + fadeOut(animationSpec = tween(300))
                        },
                        label = "authForm",
                    ) { showLogin ->
                        if (showLogin) {
                            LoginForm(navController = navController, viewModel = viewModel)
                        } else {
                            SignUpForm(navController = navController, viewModel = viewModel)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "🔧", fontSize = 16.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Soluciones rápidas y confiables",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center,
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
