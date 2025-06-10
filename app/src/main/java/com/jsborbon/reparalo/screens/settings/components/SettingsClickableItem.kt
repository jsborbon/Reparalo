package com.jsborbon.reparalo.screens.settings.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun SettingsClickableItem(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
    icon: Painter? = null,
    vectorIcon: ImageVector? = null,
    enabled: Boolean = true,
    showChevron: Boolean = true,
    showBadge: Boolean = false,
    badgeText: String? = null,
    isDestructive: Boolean = false,
    subtitle: String? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    contentDescription: String? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }

    // Animated values
    val iconScale by animateFloatAsState(
        targetValue = if (enabled) 1f else 0.8f,
        animationSpec = spring(dampingRatio = 0.7f),
        label = "icon_scale"
    )

    val titleColor by animateColorAsState(
        targetValue = when {
            !enabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            isDestructive -> MaterialTheme.colorScheme.error
            else -> MaterialTheme.colorScheme.onSurface
        },
        animationSpec = tween(durationMillis = 200),
        label = "title_color"
    )

    val iconColor by animateColorAsState(
        targetValue = when {
            !enabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            isDestructive -> MaterialTheme.colorScheme.error
            else -> MaterialTheme.colorScheme.primary
        },
        animationSpec = tween(durationMillis = 200),
        label = "icon_color"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true),
                enabled = enabled,
                role = Role.Button,
                onClickLabel = contentDescription ?: "Navigate to $title"
            ) { onClick() }
            .padding(vertical = 12.dp, horizontal = 4.dp)
            .clearAndSetSemantics {
                this.contentDescription = contentDescription ?: run {
                    buildString {
                        append(title)
                        description?.let { append(", $it") }
                        subtitle?.let { append(", $it") }
                        if (showBadge && badgeText != null) {
                            append(", badge: $badgeText")
                        }
                        if (isDestructive) append(", destructive action")
                        if (!enabled) append(", disabled")
                    }
                }
                this.role = Role.Button
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Icon with badge support
        if (icon != null || vectorIcon != null) {
            BadgedBox(
                badge = {
                    if (showBadge) {
                        if (badgeText != null) {
                            Badge(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.onError
                            ) {
                                Text(
                                    text = badgeText,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        } else {
                            Badge(
                                containerColor = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(8.dp)
                            )
                        }
                    }
                }
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = iconColor.copy(alpha = 0.1f),
                            shape = CircleShape
                        )
                        .scale(iconScale),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        icon != null -> Icon(
                            painter = icon,
                            contentDescription = null,
                            tint = iconColor,
                            modifier = Modifier.size(20.dp)
                        )
                        vectorIcon != null -> Icon(
                            imageVector = vectorIcon,
                            contentDescription = null,
                            tint = iconColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // Content Column
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = titleColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            description?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                        alpha = if (enabled) 0.7f else 0.38f
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            subtitle?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                        alpha = if (enabled) 0.6f else 0.38f
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Trailing content or chevron
        if (trailingContent != null) {
            trailingContent()
        } else if (showChevron) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                    alpha = if (enabled) 0.6f else 0.38f
                ),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun SettingsNavigationItem(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
    icon: Painter? = null,
    vectorIcon: ImageVector? = null,
    enabled: Boolean = true,
    showBadge: Boolean = false,
    badgeText: String? = null,
) {
    SettingsClickableItem(
        title = title,
        onClick = onClick,
        modifier = modifier,
        description = description,
        icon = icon,
        vectorIcon = vectorIcon,
        enabled = enabled,
        showChevron = true,
        showBadge = showBadge,
        badgeText = badgeText
    )
}

@Composable
fun SettingsToggleItem(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
    icon: Painter? = null,
    vectorIcon: ImageVector? = null,
    enabled: Boolean = true,
) {
    SettingsClickableItem(
        title = title,
        onClick = { onCheckedChange(!checked) },
        modifier = modifier,
        description = description,
        icon = icon,
        vectorIcon = vectorIcon,
        enabled = enabled,
        showChevron = false,
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                enabled = enabled
            )
        }
    )
}
