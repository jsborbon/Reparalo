package com.jsborbon.reparalo.navigation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jsborbon.reparalo.ui.theme.RepairYellow
import kotlinx.coroutines.delay

@Composable
fun BottomBarIcon(
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector? = null,
    iconPainter: Painter? = null,
    contentDescription: String,
    badgeCount: Int = 0,
    isEnabled: Boolean = true,
) {
    var isPressed by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(100)
            isPressed = false
        }
    }

    val iconScale by animateFloatAsState(
        targetValue = when {
            isPressed -> 0.85f
            selected -> 1.1f
            else -> 1f
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "iconScale"
    )

    val containerScale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "containerScale"
    )

    val iconColor by animateColorAsState(
        targetValue = when {
            !isEnabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            selected -> RepairYellow
            else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        },
        animationSpec = tween(200),
        label = "iconColor"
    )

    val textColor by animateColorAsState(
        targetValue = when {
            !isEnabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            selected -> RepairYellow
            else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        },
        animationSpec = tween(200),
        label = "textColor"
    )

    val backgroundAlpha by animateFloatAsState(
        targetValue = if (selected) 0.15f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "backgroundAlpha"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .scale(containerScale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = isEnabled,
                onClick = {
                    isPressed = true
                    onClick()
                }
            )
            .semantics {
                this.contentDescription = if (badgeCount > 0)
                    "$contentDescription, $badgeCount notificaciones"
                else contentDescription
            }
    ) {
        Box(contentAlignment = Alignment.Center) {
            Surface(
                modifier = Modifier
                    .size(if (selected) 56.dp else 48.dp)
                    .alpha(backgroundAlpha),
                shape = RoundedCornerShape(if (selected) 28.dp else 24.dp),
                color = RepairYellow.copy(alpha = 0.2f),
                shadowElevation = if (selected) 4.dp else 0.dp
            ) {
                Box(
                    modifier = Modifier.background(
                        brush = if (selected) {
                            Brush.radialGradient(
                                listOf(
                                    RepairYellow.copy(alpha = 0.3f),
                                    RepairYellow.copy(alpha = 0.1f),
                                    Color.Transparent
                                )
                            )
                        } else {
                            Brush.radialGradient(
                                listOf(Color.Transparent, Color.Transparent)
                            )
                        }
                    )
                )
            }

            // Icon: use Painter if present, else ImageVector
            AnimatedContent(
                targetState = selected,
                label = "iconAnimation",
                transitionSpec = {
                    scaleIn(initialScale = 0.8f) + fadeIn() togetherWith
                        scaleOut(targetScale = 0.8f) + fadeOut()
                }
            ) { isSelected ->
                when {
                    iconPainter != null -> {
                        Icon(
                            painter = iconPainter,
                            contentDescription = null,
                            tint = iconColor,
                            modifier = Modifier
                                .size(if (isSelected) 28.dp else 24.dp)
                                .scale(iconScale)
                        )
                    }
                    icon != null -> {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = iconColor,
                            modifier = Modifier
                                .size(if (isSelected) 28.dp else 24.dp)
                                .scale(iconScale)
                        )
                    }
                }
            }

            if (badgeCount > 0 && isEnabled) {
                AnimatedContent(
                    targetState = badgeCount,
                    label = "badgeAnimation",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 4.dp, y = (-2).dp),
                    transitionSpec = {
                        slideInVertically(
                            initialOffsetY = { -it },
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessMedium
                            )
                        ) + fadeIn() + scaleIn() togetherWith
                            slideOutVertically(targetOffsetY = { -it }) + fadeOut() + scaleOut()
                    }
                ) { count ->
                    Surface(
                        modifier = Modifier
                            .size(if (count > 9) 20.dp else 18.dp)
                            .semantics {
                                this.contentDescription = "$count notificaciones pendientes"
                            },
                        shape = RoundedCornerShape(if (count > 9) 10.dp else 9.dp),
                        color = MaterialTheme.colorScheme.error,
                        shadowElevation = 3.dp
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.background(
                                Brush.radialGradient(
                                    listOf(
                                        MaterialTheme.colorScheme.error,
                                        MaterialTheme.colorScheme.error.copy(alpha = 0.9f)
                                    )
                                )
                            )
                        ) {
                            Text(
                                text = if (count > 99) "99+" else count.toString(),
                                color = MaterialTheme.colorScheme.onError,
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = if (count > 9) 9.sp else 10.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }

        AnimatedContent(
            targetState = selected,
            label = "labelAnimation",
            transitionSpec = {
                fadeIn(tween(300)) togetherWith fadeOut(tween(200))
            }
        ) { isSelected ->
            Text(
                text = contentDescription,
                style = if (isSelected)
                    MaterialTheme.typography.labelMedium
                else
                    MaterialTheme.typography.labelSmall,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = textColor,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = if (isSelected) 11.sp else 10.sp,
                lineHeight = 12.sp,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .alpha(if (isEnabled) 1f else 0.5f)
            )
        }
    }
}
