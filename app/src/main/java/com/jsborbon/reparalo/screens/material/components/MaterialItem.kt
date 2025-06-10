package com.jsborbon.reparalo.screens.material.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jsborbon.reparalo.R
import com.jsborbon.reparalo.models.Material
import com.jsborbon.reparalo.ui.theme.PrimaryLight
import com.jsborbon.reparalo.ui.theme.RepairYellow
import com.jsborbon.reparalo.ui.theme.Success
import com.jsborbon.reparalo.utils.formatPrice

enum class MaterialItemStyle {
    STANDARD,   // Full information display
    COMPACT,    // Condensed version for lists
    DETAILED    // Extended information with quantity
}

@Composable
fun MaterialItem(
    modifier: Modifier = Modifier,
    material: Material,
    onClick: (() -> Unit)? = null,
    style: MaterialItemStyle = MaterialItemStyle.STANDARD,
    showQuantity: Boolean = true,
    isSelected: Boolean = false,
    isAvailable: Boolean = true,
    customIcon: ImageVector? = null
) {
    // Enhanced animation states
    val animatedScale by animateFloatAsState(
        targetValue = if (isSelected) 1.02f else 1.0f,
        animationSpec = tween(durationMillis = 200),
        label = "materialScale"
    )

    val animatedBackgroundColor by animateColorAsState(
        targetValue = when {
            isSelected -> PrimaryLight.copy(alpha = 0.1f)
            !isAvailable -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            else -> MaterialTheme.colorScheme.surface
        },
        animationSpec = tween(durationMillis = 200),
        label = "materialBackgroundColor"
    )

    val animatedContentAlpha by animateFloatAsState(
        targetValue = if (isAvailable) 1.0f else 0.6f,
        animationSpec = tween(durationMillis = 200),
        label = "materialContentAlpha"
    )

    // Clickable modifier
    val clickableModifier = if (onClick != null && isAvailable) {
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
        append("Material: ${material.name}")
        if (material.description.isNotEmpty()) {
            append(", ${material.description}")
        }
        append(", precio: ${formatPrice(material.price)}")
        if (showQuantity && material.quantity > 0) {
            append(", cantidad: ${material.quantity}")
        }
        if (isSelected) append(", seleccionado")
        if (!isAvailable) append(", no disponible")
        if (onClick != null) append(", toca para ver detalles")
    }

    when (style) {
        MaterialItemStyle.STANDARD -> {
            StandardMaterialItem(
                material = material,
                animatedBackgroundColor = animatedBackgroundColor,
                animatedScale = animatedScale,
                animatedContentAlpha = animatedContentAlpha,
                clickableModifier = clickableModifier,
                semanticDescription = semanticDescription,
                modifier = modifier,
                showQuantity = showQuantity,
                isSelected = isSelected,
                customIcon = customIcon
            )
        }

        MaterialItemStyle.COMPACT -> {
            CompactMaterialItem(
                material = material,
                animatedBackgroundColor = animatedBackgroundColor,
                animatedScale = animatedScale,
                animatedContentAlpha = animatedContentAlpha,
                clickableModifier = clickableModifier,
                semanticDescription = semanticDescription,
                modifier = modifier,
                isSelected = isSelected,
                customIcon = customIcon
            )
        }

        MaterialItemStyle.DETAILED -> {
            DetailedMaterialItem(
                material = material,
                animatedBackgroundColor = animatedBackgroundColor,
                animatedScale = animatedScale,
                animatedContentAlpha = animatedContentAlpha,
                clickableModifier = clickableModifier,
                semanticDescription = semanticDescription,
                modifier = modifier,
                showQuantity = showQuantity,
                isSelected = isSelected,
                customIcon = customIcon
            )
        }
    }
}

@Composable
private fun StandardMaterialItem(
    material: Material,
    animatedBackgroundColor: Color,
    animatedScale: Float,
    animatedContentAlpha: Float,
    clickableModifier: Modifier,
    semanticDescription: String,
    modifier: Modifier,
    showQuantity: Boolean,
    isSelected: Boolean,
    customIcon: ImageVector?
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .scale(animatedScale)
            .then(clickableModifier)
            .semantics { contentDescription = semanticDescription },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = animatedBackgroundColor
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Enhanced icon with background
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = PrimaryLight.copy(alpha = 0.2f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = customIcon ?: Icons.Default.Build,
                    contentDescription = null,
                    tint = PrimaryLight.copy(alpha = animatedContentAlpha),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Enhanced content section
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = material.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = animatedContentAlpha),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (material.description.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = material.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = animatedContentAlpha),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Quantity display
                if (showQuantity && material.quantity > 0) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id=R.drawable.outline_numbers),
                            contentDescription = null,
                            tint = RepairYellow.copy(alpha = animatedContentAlpha),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Cantidad: ${material.quantity}",
                            style = MaterialTheme.typography.bodySmall,
                            color = RepairYellow.copy(alpha = animatedContentAlpha),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Price display
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_euro),
                        contentDescription = null,
                        tint = Success.copy(alpha = animatedContentAlpha),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = formatPrice(material.price),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Success.copy(alpha = animatedContentAlpha)
                    )
                }

                if (showQuantity && material.quantity > 1) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Total: ${formatPrice(material.price * material.quantity)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = animatedContentAlpha)
                    )
                }
            }
        }
    }
}

@Composable
private fun CompactMaterialItem(
    material: Material,
    animatedBackgroundColor: Color,
    animatedScale: Float,
    animatedContentAlpha: Float,
    clickableModifier: Modifier,
    semanticDescription: String,
    modifier: Modifier,
    isSelected: Boolean,
    customIcon: ImageVector?
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .scale(animatedScale)
            .then(clickableModifier)
            .semantics { contentDescription = semanticDescription },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 3.dp else 1.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = animatedBackgroundColor
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = customIcon ?: Icons.Default.Build,
                    contentDescription = null,
                    tint = PrimaryLight.copy(alpha = animatedContentAlpha),
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = material.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = animatedContentAlpha),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Text(
                text = formatPrice(material.price),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = Success.copy(alpha = animatedContentAlpha)
            )
        }
    }
}

@Composable
private fun DetailedMaterialItem(
    material: Material,
    animatedBackgroundColor: Color,
    animatedScale: Float,
    animatedContentAlpha: Float,
    clickableModifier: Modifier,
    semanticDescription: String,
    modifier: Modifier,
    showQuantity: Boolean,
    isSelected: Boolean,
    customIcon: ImageVector?
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .scale(animatedScale)
            .then(clickableModifier)
            .semantics { contentDescription = semanticDescription },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 6.dp else 3.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = animatedBackgroundColor
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            color = PrimaryLight.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = customIcon ?: Icons.Default.Build,
                        contentDescription = null,
                        tint = PrimaryLight.copy(alpha = animatedContentAlpha),
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = material.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = animatedContentAlpha)
                    )

                    if (material.description.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = material.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = animatedContentAlpha)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Details section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (showQuantity && material.quantity > 0) {
                    Column {
                        Text(
                            text = "Cantidad",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = animatedContentAlpha),
                            fontWeight = FontWeight.Medium
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.outline_numbers),
                                contentDescription = null,
                                tint = RepairYellow.copy(alpha = animatedContentAlpha),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = material.quantity.toString(),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = RepairYellow.copy(alpha = animatedContentAlpha)
                            )
                        }
                    }
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "Precio unitario",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = animatedContentAlpha),
                        fontWeight = FontWeight.Medium
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_euro),
                            contentDescription = null,
                            tint = Success.copy(alpha = animatedContentAlpha),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = formatPrice(material.price),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Success.copy(alpha = animatedContentAlpha)
                        )
                    }
                }
            }

            if (showQuantity && material.quantity > 1) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "Total: ${formatPrice(material.price * material.quantity)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = animatedContentAlpha)
                    )
                }
            }
        }
    }
}


