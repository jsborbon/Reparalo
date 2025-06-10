package com.jsborbon.reparalo.screens.splashScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.jsborbon.reparalo.R
import com.jsborbon.reparalo.ui.theme.PrimaryDark
import com.jsborbon.reparalo.ui.theme.PrimaryLight
import com.jsborbon.reparalo.ui.theme.Secondary

@Composable
fun LogoContainer(
    pulseScale: Float,
    rotationAngle: Float,
) {
    Box(
        modifier = Modifier
            .size(96.dp)
            .scale(pulseScale)
            .clip(CircleShape)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(Secondary, PrimaryLight, PrimaryDark),
                    radius = 120f
                )
            )
            .semantics {
                contentDescription = "Logo de Reparalo"
            },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_reparalo_logo),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(52.dp)
                .rotate(rotationAngle)
                .graphicsLayer {
                    shadowElevation = 8f
                },
        )
    }
}

