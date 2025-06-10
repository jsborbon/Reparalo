package com.jsborbon.reparalo.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun InfoCard(
    modifier: Modifier = Modifier,
    title: String,
    icon: String? = null, // Emoji icon for the card
    subtitle: String? = null, // Optional subtitle for additional context
    cardType: InfoCardType = InfoCardType.DEFAULT,
    animateOnAppear: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {
    // Enhanced state management for animations
    var isVisible by remember { mutableStateOf(!animateOnAppear) }

    // Enhanced scale animation for better interaction feedback
    val cardScale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.95f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "cardScale"
    )

    // Trigger appearance animation
    LaunchedEffect(Unit) {
        if (animateOnAppear) {
            delay(100) // Small delay for staggered appearance
            isVisible = true
        }
    }

    // Enhanced card colors based on type
    val cardColors = when (cardType) {
        InfoCardType.DEFAULT -> CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        )
        InfoCardType.PRIMARY -> CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
        )
        InfoCardType.SECONDARY -> CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f),
        )
        InfoCardType.SUCCESS -> CardDefaults.cardColors(
            containerColor = Color(0xFF4CAF50).copy(alpha = 0.1f),
        )
        InfoCardType.WARNING -> CardDefaults.cardColors(
            containerColor = Color(0xFFFFC107).copy(alpha = 0.1f),
        )
        InfoCardType.ERROR -> CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f),
        )
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            initialOffsetY = { it / 3 },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        ) + fadeIn(
            animationSpec = tween(durationMillis = 500)
        ) + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 4.dp)
                .scale(cardScale)
                .semantics {
                    contentDescription = "Tarjeta de información: $title${subtitle?.let { ", $it" } ?: ""}"
                },
            shape = RoundedCornerShape(20.dp),
            colors = cardColors,
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp,
                pressedElevation = 2.dp
            )
        ) {
            Box {
                // Enhanced gradient overlay for visual depth
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = when (cardType) {
                                    InfoCardType.PRIMARY -> listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                                    )
                                    InfoCardType.SECONDARY -> listOf(
                                        MaterialTheme.colorScheme.secondary,
                                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f)
                                    )
                                    InfoCardType.SUCCESS -> listOf(
                                        Color(0xFF4CAF50),
                                        Color(0xFF4CAF50).copy(alpha = 0.6f)
                                    )
                                    InfoCardType.WARNING -> listOf(
                                        Color(0xFFFFC107),
                                        Color(0xFFFFC107).copy(alpha = 0.6f)
                                    )
                                    InfoCardType.ERROR -> listOf(
                                        MaterialTheme.colorScheme.error,
                                        MaterialTheme.colorScheme.error.copy(alpha = 0.6f)
                                    )
                                    else -> listOf(
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                        Color.Transparent
                                    )
                                }
                            )
                        )
                )

                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    // Enhanced header with icon support
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Icon display if provided
                        icon?.let { iconText ->
                            Surface(
                                modifier = Modifier.size(40.dp),
                                shape = RoundedCornerShape(20.dp),
                                color = when (cardType) {
                                    InfoCardType.PRIMARY -> MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                    InfoCardType.SECONDARY -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                                    InfoCardType.SUCCESS -> Color(0xFF4CAF50).copy(alpha = 0.1f)
                                    InfoCardType.WARNING -> Color(0xFFFFC107).copy(alpha = 0.1f)
                                    InfoCardType.ERROR -> MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
                                    else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                }
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .semantics {
                                            contentDescription = "Icono de la tarjeta"
                                        }
                                ) {
                                    Text(
                                        text = iconText,
                                        fontSize = 20.sp
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                        }

                        // Title and subtitle column
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = when (cardType) {
                                    InfoCardType.ERROR -> MaterialTheme.colorScheme.error
                                    InfoCardType.SUCCESS -> Color(0xFF2E7D32)
                                    InfoCardType.WARNING -> Color(0xFFE65100)
                                    else -> MaterialTheme.colorScheme.onSurface
                                },
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 18.sp,
                                lineHeight = 22.sp,
                                modifier = Modifier.semantics {
                                    contentDescription = "Título de la tarjeta: $title"
                                }
                            )

                            // Enhanced subtitle display
                            subtitle?.let { subtitleText ->
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = subtitleText,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                    fontSize = 14.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.semantics {
                                        contentDescription = "Subtítulo: $subtitleText"
                                    }
                                )
                            }
                        }
                    }

                    // Enhanced spacing between header and content
                    Spacer(modifier = Modifier.height(if (subtitle != null) 16.dp else 12.dp))

                    // Enhanced content section with better visual separation
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = Color.Transparent
                    ) {
                        Column(
                            modifier = Modifier.semantics {
                                contentDescription = "Contenido de la tarjeta"
                            }
                        ) {
                            content()
                        }
                    }
                }
            }
        }
    }
}

/**
 * Enhanced card types for different use cases and visual styling
 */
enum class InfoCardType {
    DEFAULT,    // Standard card with neutral colors
    PRIMARY,    // Primary theme colored card
    SECONDARY,  // Secondary theme colored card
    SUCCESS,    // Green-themed card for success states
    WARNING,    // Yellow-themed card for warnings
    ERROR       // Red-themed card for errors
}
