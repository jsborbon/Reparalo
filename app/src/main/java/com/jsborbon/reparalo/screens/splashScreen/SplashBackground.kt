package com.jsborbon.reparalo.screens.splashScreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import com.jsborbon.reparalo.ui.theme.Accent
import com.jsborbon.reparalo.ui.theme.RepairYellow
import com.jsborbon.reparalo.ui.theme.Secondary

@Composable
 fun SplashBackground(shimmerProgress: Float) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        val shimmerAlpha = (0.02f + shimmerProgress * 0.03f)

        drawCircle(
            color = Color.White.copy(alpha = shimmerAlpha),
            radius = width * 0.35f,
            center = Offset(width * 0.85f, height * 0.15f),
        )
        drawCircle(
            color = RepairYellow.copy(alpha = shimmerAlpha * 0.8f),
            radius = width * 0.25f,
            center = Offset(width * 0.15f, height * 0.85f),
        )

        drawLine(
            brush = Brush.linearGradient(
                colors = listOf(
                    Secondary.copy(alpha = 0.15f),
                    Secondary.copy(alpha = 0.05f)
                )
            ),
            start = Offset(0f, height * 0.25f),
            end = Offset(width, height * 0.75f),
            strokeWidth = 6f,
            cap = StrokeCap.Round,
        )
        drawLine(
            brush = Brush.linearGradient(
                colors = listOf(
                    Accent.copy(alpha = 0.12f),
                    Accent.copy(alpha = 0.03f)
                )
            ),
            start = Offset(0f, height * 0.75f),
            end = Offset(width, height * 0.25f),
            strokeWidth = 4f,
            cap = StrokeCap.Round,
        )
    }
}
