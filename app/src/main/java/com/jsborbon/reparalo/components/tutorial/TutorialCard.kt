package com.jsborbon.reparalo.components.tutorial

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.jsborbon.reparalo.models.Tutorial
import com.jsborbon.reparalo.ui.theme.RepairYellow

@Composable
fun TutorialCard(
    tutorial: Tutorial,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onDeleteClick: (() -> Unit)? = null,
    onItemClick: () -> Unit,
    currentUserId: String? = null,
) {
    // Enhanced state management for interactions
    var isPressed by remember { mutableStateOf(false) }
    val isOwner = tutorial.author.uid == currentUserId

    // Enhanced animations for better feedback
    val cardScale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "cardScale"
    )

    val favoriteIconColor by animateColorAsState(
        targetValue = if (isFavorite) RepairYellow else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
        animationSpec = tween(durationMillis = 300),
        label = "favoriteColor"
    )

    val favoriteScale by animateFloatAsState(
        targetValue = if (isFavorite) 1.1f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "favoriteScale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(cardScale)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    isPressed = true
                    onItemClick()
                }
            )
            .semantics {
                contentDescription = "Tutorial: ${tutorial.title} por ${tutorial.author.name}"
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
            pressedElevation = 2.dp
        )
    ) {
        Box {
            // Enhanced content layout with better hierarchy
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                // Enhanced header with play indicator
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // Main content area
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        // Enhanced title with better typography
                        Text(
                            text = tutorial.title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = 24.sp,
                            modifier = Modifier.semantics {
                                contentDescription = "Título del tutorial: ${tutorial.title}"
                            }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Enhanced description with better readability
                        Text(
                            text = tutorial.description,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = 20.sp,
                            modifier = Modifier.semantics {
                                contentDescription = "Descripción: ${tutorial.description}"
                            }
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Enhanced play indicator
                    Surface(
                        modifier = Modifier
                            .size(48.dp)
                            .semantics {
                                contentDescription = "Reproducir tutorial"
                            },
                        shape = RoundedCornerShape(24.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shadowElevation = 2.dp
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                                        )
                                    )
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.PlayArrow,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Enhanced bottom section with better visual separation
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        // Enhanced author section with icon
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Surface(
                                modifier = Modifier.size(32.dp),
                                shape = RoundedCornerShape(16.dp),
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Person,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .size(18.dp)
                                        .padding(7.dp)
                                )
                            }

                            Column {
                                Text(
                                    text = "Autor",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                    fontSize = 10.sp
                                )
                                Text(
                                    text = tutorial.author.name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.semantics {
                                        contentDescription = "Autor: ${tutorial.author.name}"
                                    }
                                )
                            }
                        }

                        // Enhanced action buttons with better spacing
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Enhanced favorite button with animation
                            Surface(
                                modifier = Modifier
                                    .size(44.dp)
                                    .scale(favoriteScale),
                                shape = RoundedCornerShape(22.dp),
                                color = if (isFavorite)
                                    RepairYellow.copy(alpha = 0.1f)
                                else
                                    Color.Transparent
                            ) {
                                IconButton(
                                    onClick = onFavoriteClick,
                                    modifier = Modifier.semantics {
                                        contentDescription = if (isFavorite)
                                            "Quitar de favoritos"
                                        else
                                            "Agregar a favoritos"
                                    }
                                ) {
                                    AnimatedContent(
                                        targetState = isFavorite,
                                        label = "favoriteIcon",
                                        transitionSpec = {
                                            slideInHorizontally(
                                                initialOffsetX = { it / 4 }
                                            ) + fadeIn() togetherWith slideOutHorizontally(
                                                targetOffsetX = { -it / 4 }
                                            ) + fadeOut()
                                        }
                                    ) { favorite ->
                                        Icon(
                                            imageVector = if (favorite)
                                                Icons.Filled.Star
                                            else
                                                Icons.Outlined.Star,
                                            contentDescription = null,
                                            tint = favoriteIconColor,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }
                            }

                            // Enhanced delete button for owners
                            if (isOwner && onDeleteClick != null) {
                                Surface(
                                    modifier = Modifier.size(44.dp),
                                    shape = RoundedCornerShape(22.dp),
                                    color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f)
                                ) {
                                    IconButton(
                                        onClick = onDeleteClick,
                                        modifier = Modifier.semantics {
                                            contentDescription = "Eliminar tutorial propio"
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Enhanced owner indicator badge
            if (isOwner) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .semantics {
                            contentDescription = "Tutorial propio"
                        },
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primary,
                    shadowElevation = 4.dp
                ) {
                    Text(
                        text = "Tuyo",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}
