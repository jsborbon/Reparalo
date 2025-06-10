package com.jsborbon.reparalo.screens.technician.components.cardComponents.statisticItems

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.jsborbon.reparalo.ui.theme.Success

@Composable
fun ProgressBar(progress: Float) {
    val animatedProgress by rememberInfiniteTransition(label = "progressBar").animateFloat(
        initialValue = 0f,
        targetValue = progress,
        animationSpec = infiniteRepeatable(tween(1500), RepeatMode.Restart),
        label = "animatedProgress"
    )

    LinearProgressIndicator(
        progress = { animatedProgress },
        modifier = Modifier
            .width(40.dp)
            .height(4.dp)
            .clip(RoundedCornerShape(2.dp))
            .semantics {
                contentDescription = "Nivel de progreso: ${(progress * 100).toInt()}%"
            },
        color = Success,
        trackColor = Success.copy(alpha = 0.2f)
    )
}
