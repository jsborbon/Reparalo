package com.jsborbon.reparalo.screens.technician.components.cardComponents

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jsborbon.reparalo.screens.technician.components.cardComponents.statisticItems.IconWithGradient
import com.jsborbon.reparalo.screens.technician.components.cardComponents.statisticItems.ProgressBar
import com.jsborbon.reparalo.screens.technician.components.cardComponents.statisticItems.RatingStars
import kotlinx.coroutines.delay

@Composable
fun StatisticItem(
    icon: ImageVector,
    label: String,
    subtitle: String,
    value: String,
    iconTint: Color,
    modifier: Modifier = Modifier,
    animationDelay: Long = 0L,
    showStars: Boolean = false,
    starsValue: Float = 0f,
    showProgressBar: Boolean = false,
    progressValue: Float = 0f
) {
    var isVisible by remember { mutableStateOf(false) }
    val scaleAnim = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        delay(animationDelay)
        isVisible = true
        scaleAnim.animateTo(
            targetValue = 1f,
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
        )
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(300)) + scaleIn()
    ) {
        Card(
            modifier = modifier
                .scale(scaleAnim.value)
                .animateContentSize()
                .semantics { contentDescription = "$label: $value" },
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                IconWithGradient(icon = icon, iconTint = iconTint)

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )

                if (showStars) {
                    Spacer(modifier = Modifier.height(4.dp))
                    RatingStars(starsValue)
                }

                if (showProgressBar) {
                    Spacer(modifier = Modifier.height(6.dp))
                    ProgressBar(progress = progressValue)
                }
            }
        }
    }
}
