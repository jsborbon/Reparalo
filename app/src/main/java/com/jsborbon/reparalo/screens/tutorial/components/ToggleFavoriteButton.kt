package com.jsborbon.reparalo.screens.tutorial.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.jsborbon.reparalo.ui.theme.RepairYellow

/**
 * Animated favorite button with haptic feedback and accessibility support.
 *
 * @param tutorialId Unique identifier for the tutorial
 * @param favorites Set of favorite tutorial IDs
 * @param onToggle Callback when favorite status is toggled
 * @param modifier Optional Modifier for customizing the component layout
 */
@Composable
fun ToggleFavoriteButton(
    tutorialId: String,
    favorites: Set<String>,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isFavorite = tutorialId in favorites
    val haptic = LocalHapticFeedback.current
    var animate by remember { mutableStateOf(false) }

    // Animation for button press
    val scale by animateFloatAsState(
        targetValue = if (animate) 1.3f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "favoriteScale"
    )

    // Reset animation after it plays
    LaunchedEffect(animate) {
        if (animate) {
            animate = false
        }
    }

    IconButton(
        onClick = {
            animate = true
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            onToggle()
        },
        modifier = modifier.semantics {
            contentDescription = if (isFavorite)
                "Eliminar de favoritos" else "Añadir a favoritos"
            role = Role.Button
        }
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = null,
            tint = if (isFavorite) RepairYellow else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .size(28.dp)
                .scale(scale)
        )
    }
}
