package com.jsborbon.reparalo.screens.technician.components.cardComponents.statisticItems

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.jsborbon.reparalo.ui.theme.RepairYellow


@Composable
fun RatingStars(rating: Float) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.semantics {
            contentDescription = "Calificación de $rating estrellas de 5"
        }
    ) {
        repeat(5) { index ->
            val starColor by animateColorAsState(
                targetValue = if (index < rating) RepairYellow else Color.Gray.copy(alpha = 0.3f),
                animationSpec = tween(300),
                label = "starColor"
            )

            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = starColor,
                modifier = Modifier.size(12.dp)
            )
        }
    }
}

