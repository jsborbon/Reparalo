package com.jsborbon.reparalo.screens.material.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jsborbon.reparalo.ui.theme.PrimaryLight

// Enhanced category chip with multiple layout options
enum class CategoryChipStyle {
    VERTICAL,   // Icon above text (original style)
    HORIZONTAL, // Icon beside text
    COMPACT     // Small version for limited space
}

@Composable
fun CategoryChip(
    icon: ImageVector,
    text: String,
    color: Color = PrimaryLight,
    modifier: Modifier = Modifier,
    style: CategoryChipStyle = CategoryChipStyle.VERTICAL,
    isSelected: Boolean = false,
    isEnabled: Boolean = true,
    onClick: (() -> Unit)? = null,
    showBorder: Boolean = false,
    maxLines: Int = 1
) {
    // Enhanced animation states
    val animatedScale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1.0f,
        animationSpec = tween(durationMillis = 200),
        label = "chipScale"
    )

    val animatedBackgroundColor by animateColorAsState(
        targetValue = if (isSelected) {
            color.copy(alpha = 0.2f)
        } else {
            color.copy(alpha = 0.1f)
        },
        animationSpec = tween(durationMillis = 200),
        label = "chipBackgroundColor"
    )

    val animatedContentColor by animateColorAsState(
        targetValue = if (isEnabled) {
            if (isSelected) color else color.copy(alpha = 0.8f)
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        },
        animationSpec = tween(durationMillis = 200),
        label = "chipContentColor"
    )

    val animatedBorderColor by animateColorAsState(
        targetValue = if (showBorder && isSelected) {
            color
        } else {
            Color.Transparent
        },
        animationSpec = tween(durationMillis = 200),
        label = "chipBorderColor"
    )

    // Clickable modifier with proper interaction handling
    val clickableModifier = if (onClick != null && isEnabled) {
        Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        )
    } else {
        Modifier
    }

    // Enhanced semantic description
    val semanticDescription = buildString {
        append("Categoría $text")
        if (isSelected) append(", seleccionada")
        if (!isEnabled) append(", deshabilitada")
        if (onClick != null) append(", toca para seleccionar")
    }

    when (style) {
        CategoryChipStyle.VERTICAL -> {
            VerticalCategoryChip(
                icon = icon,
                text = text,
                animatedBackgroundColor = animatedBackgroundColor,
                animatedContentColor = animatedContentColor,
                animatedBorderColor = animatedBorderColor,
                animatedScale = animatedScale,
                clickableModifier = clickableModifier,
                semanticDescription = semanticDescription,
                modifier = modifier,
                maxLines = maxLines,
                isSelected = isSelected
            )
        }

        CategoryChipStyle.HORIZONTAL -> {
            HorizontalCategoryChip(
                icon = icon,
                text = text,
                animatedBackgroundColor = animatedBackgroundColor,
                animatedContentColor = animatedContentColor,
                animatedScale = animatedScale,
                clickableModifier = clickableModifier,
                semanticDescription = semanticDescription,
                modifier = modifier,
                maxLines = maxLines,
                isSelected = isSelected
            )
        }

        CategoryChipStyle.COMPACT -> {
            CompactCategoryChip(
                icon = icon,
                text = text,
                animatedBackgroundColor = animatedBackgroundColor,
                animatedContentColor = animatedContentColor,
                animatedScale = animatedScale,
                clickableModifier = clickableModifier,
                semanticDescription = semanticDescription,
                modifier = modifier,
                maxLines = maxLines,
                isSelected = isSelected
            )
        }
    }
}

@Composable
private fun VerticalCategoryChip(
    icon: ImageVector,
    text: String,
    animatedBackgroundColor: Color,
    animatedContentColor: Color,
    animatedBorderColor: Color,
    animatedScale: Float,
    clickableModifier: Modifier,
    semanticDescription: String,
    modifier: Modifier,
    maxLines: Int,
    isSelected: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .scale(animatedScale)
            .clip(RoundedCornerShape(12.dp))
            .background(
                color = animatedBackgroundColor,
                shape = RoundedCornerShape(12.dp)
            )
            .then(
                if (animatedBorderColor != Color.Transparent) {
                    Modifier.background(
                        color = Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    )
                } else Modifier
            )
            .then(clickableModifier)
            .padding(12.dp)
            .semantics { contentDescription = semanticDescription },
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = animatedContentColor,
            modifier = Modifier.size(if (isSelected) 28.dp else 24.dp),
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = text,
            color = animatedContentColor,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun HorizontalCategoryChip(
    icon: ImageVector,
    text: String,
    animatedBackgroundColor: Color,
    animatedContentColor: Color,
    animatedScale: Float,
    clickableModifier: Modifier,
    semanticDescription: String,
    modifier: Modifier,
    maxLines: Int,
    isSelected: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .scale(animatedScale)
            .clip(RoundedCornerShape(20.dp))
            .background(
                color = animatedBackgroundColor,
                shape = RoundedCornerShape(20.dp)
            )
            .then(clickableModifier)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .semantics { contentDescription = semanticDescription },
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = animatedContentColor,
            modifier = Modifier.size(if (isSelected) 20.dp else 18.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = animatedContentColor,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun CompactCategoryChip(
    icon: ImageVector,
    text: String,
    animatedBackgroundColor: Color,
    animatedContentColor: Color,
    animatedScale: Float,
    clickableModifier: Modifier,
    semanticDescription: String,
    modifier: Modifier,
    maxLines: Int,
    isSelected: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .scale(animatedScale)
            .clip(RoundedCornerShape(8.dp))
            .background(
                color = animatedBackgroundColor,
                shape = RoundedCornerShape(8.dp)
            )
            .then(clickableModifier)
            .padding(8.dp)
            .semantics { contentDescription = semanticDescription },
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = animatedContentColor,
            modifier = Modifier.size(if (isSelected) 18.dp else 16.dp),
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = text,
            color = animatedContentColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis
        )
    }
}
