package com.jsborbon.reparalo.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun InfoRow(
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    iconVector: ImageVector? = null,
    iconEmoji: String? = null,
    text: String,
    subtitle: String? = null,
    value: String? = null,
    isClickable: Boolean = false,
    isImportant: Boolean = false,
    showDivider: Boolean = false,
    animateOnAppear: Boolean = true,
    onClick: (() -> Unit)? = null,
) {
    // Enhanced state management for animations and interactions
    var isVisible by remember { mutableStateOf(!animateOnAppear) }
    var isPressed by remember { mutableStateOf(false) }

    // Enhanced animations for better visual feedback
    val rowScale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "rowScale"
    )

    val iconScale by animateFloatAsState(
        targetValue = if (isImportant) 1.1f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "iconScale"
    )

    // Trigger appearance animation
    LaunchedEffect(Unit) {
        if (animateOnAppear) {
            delay(50) // Small delay for staggered appearance
            isVisible = true
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInHorizontally(
            initialOffsetX = { -it / 3 },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        ) + fadeIn(
            animationSpec = tween(durationMillis = 400)
        )
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .scale(rowScale)
                .then(
                    if (isClickable && onClick != null) {
                        Modifier
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = {
                                    isPressed = true
                                    onClick()
                                }
                            )
                            .semantics {
                                contentDescription = "Elemento clickeable: $text${subtitle?.let { ", $it" } ?: ""}"
                            }
                    } else {
                        Modifier.semantics {
                            contentDescription = "Información: $text${subtitle?.let { ", $it" } ?: ""}"
                        }
                    }
                ),
            shape = RoundedCornerShape(12.dp),
            color = if (isClickable)
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            else
                Color.Transparent,
            shadowElevation = if (isImportant) 2.dp else 0.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = if (isClickable) 12.dp else 0.dp,
                        vertical = if (isClickable) 12.dp else 8.dp
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Enhanced icon section with multiple input types
                Box {
                    when {
                        // Vector icon support
                        iconVector != null -> {
                            Surface(
                                modifier = Modifier
                                    .size(if (isImportant) 44.dp else 40.dp)
                                    .scale(iconScale),
                                shape = RoundedCornerShape(if (isImportant) 22.dp else 20.dp),
                                color = if (isImportant)
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                else
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .background(
                                            brush = if (isImportant) {
                                                Brush.radialGradient(
                                                    colors = listOf(
                                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                                                    )
                                                )
                                            } else {
                                                Brush.radialGradient(
                                                    colors = listOf(
                                                        Color.Transparent,
                                                        Color.Transparent
                                                    )
                                                )
                                            }
                                        )
                                ) {
                                    Icon(
                                        imageVector = iconVector,
                                        contentDescription = null,
                                        tint = if (isImportant)
                                            MaterialTheme.colorScheme.primary
                                        else
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                        modifier = Modifier.size(
                                            if (isImportant) 24.dp else 20.dp
                                        )
                                    )
                                }
                            }
                        }

                        // Painter icon support (original)
                        icon != null -> {
                            Surface(
                                modifier = Modifier
                                    .size(if (isImportant) 44.dp else 40.dp)
                                    .scale(iconScale),
                                shape = RoundedCornerShape(if (isImportant) 22.dp else 20.dp),
                                color = if (isImportant)
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                else
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        painter = icon,
                                        contentDescription = null,
                                        tint = if (isImportant)
                                            MaterialTheme.colorScheme.primary
                                        else
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                        modifier = Modifier.size(
                                            if (isImportant) 24.dp else 20.dp
                                        )
                                    )
                                }
                            }
                        }

                        // Emoji icon support
                        iconEmoji != null -> {
                            Surface(
                                modifier = Modifier
                                    .size(if (isImportant) 44.dp else 40.dp)
                                    .scale(iconScale),
                                shape = RoundedCornerShape(if (isImportant) 22.dp else 20.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.semantics {
                                        contentDescription = "Icono emoji"
                                    }
                                ) {
                                    Text(
                                        text = iconEmoji,
                                        fontSize = if (isImportant) 20.sp else 18.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // Enhanced text content section
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Main content column
                    androidx.compose.foundation.layout.Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        // Enhanced main text with better typography
                        Text(
                            text = text,
                            style = if (isImportant)
                                MaterialTheme.typography.titleSmall
                            else
                                MaterialTheme.typography.bodyLarge,
                            fontWeight = if (isImportant) FontWeight.Bold else FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = if (subtitle != null) 18.sp else 20.sp,
                            modifier = Modifier.semantics {
                                contentDescription = "Texto principal: $text"
                            }
                        )

                        // Enhanced subtitle support
                        subtitle?.let { subtitleText ->
                            Text(
                                text = subtitleText,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 12.sp,
                                modifier = Modifier
                                    .padding(top = 2.dp)
                                    .semantics {
                                        contentDescription = "Subtítulo: $subtitleText"
                                    }
                            )
                        }
                    }

                    // Enhanced value display with animation
                    value?.let { valueText ->
                        Spacer(modifier = Modifier.width(8.dp))
                        AnimatedContent(
                            targetState = valueText,
                            label = "valueChange",
                            transitionSpec = {
                                slideInHorizontally(
                                    initialOffsetX = { it / 4 }
                                ) + fadeIn() togetherWith slideOutHorizontally(
                                    targetOffsetX = { -it / 4 }
                                ) + fadeOut()
                            }
                        ) { animatedValue ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isImportant)
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                else
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            ) {
                                Text(
                                    text = animatedValue,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isImportant) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isImportant)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                        .semantics {
                                            contentDescription = "Valor: $animatedValue"
                                        }
                                )
                            }
                        }
                    }
                }

                // Enhanced clickable indicator with animation
                AnimatedVisibility(
                    visible = isClickable,
                    enter = fadeIn() + slideInHorizontally(initialOffsetX = { it / 2 }),
                    exit = fadeOut() + slideOutHorizontally(targetOffsetX = { it / 2 })
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Navegable",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        modifier = Modifier
                            .size(20.dp)
                            .alpha(0.7f)
                    )
                }
            }
        }

        // Enhanced divider with better styling
        if (showDivider) {
            androidx.compose.foundation.layout.Column {
                Spacer(modifier = Modifier.size(8.dp))
                androidx.compose.material3.HorizontalDivider(
                    modifier = Modifier.padding(horizontal = if (icon != null || iconVector != null || iconEmoji != null) 52.dp else 0.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                )
                Spacer(modifier = Modifier.size(8.dp))
            }
        }
    }
}

