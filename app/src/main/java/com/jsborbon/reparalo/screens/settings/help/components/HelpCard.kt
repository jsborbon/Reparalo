package com.jsborbon.reparalo.screens.settings.help.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jsborbon.reparalo.R
import com.jsborbon.reparalo.models.HelpItem
import kotlinx.coroutines.delay


@Composable
fun HelpCard(
    item: HelpItem,
    modifier: Modifier = Modifier,
    isExpandedByDefault: Boolean = false,
    animationDelay: Long = 0L,
    onExpansionChange: ((Boolean) -> Unit)? = null,
) {
    // Animation and expansion states
    var isVisible by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(isExpandedByDefault) }
    val interactionSource = remember { MutableInteractionSource() }

    // Trigger entrance animation with delay
    LaunchedEffect(Unit) {
        delay(animationDelay)
        isVisible = true
    }

    // Notify parent of expansion changes
    LaunchedEffect(isExpanded) {
        onExpansionChange?.invoke(isExpanded)
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(
            animationSpec = tween(durationMillis = 400)
        ) + slideInHorizontally(
            animationSpec = tween(durationMillis = 400)
        ) { -it / 2 }
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClickLabel = if (isExpanded) "Contraer información" else "Expandir información"
                ) {
                    isExpanded = !isExpanded
                }
                .semantics {
                    role = Role.Button
                    contentDescription = "Elemento de ayuda: ${item.title}. " +
                        if (isExpanded) "Expandido. Toca para contraer"
                        else "Contraído. Toca para expandir"
                },
            elevation = CardDefaults.cardElevation(
                defaultElevation = 3.dp,
                pressedElevation = 1.dp,
                hoveredElevation = 5.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                // Header section with icon, title, and expand button
                HelpCardHeader(
                    title = item.title,
                    isExpanded = isExpanded,
                    onExpandClick = { isExpanded = !isExpanded }
                )

                // Expandable description section
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = expandVertically(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    ) + fadeIn()
                ) {
                    HelpCardContent(
                        description = item.description,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }

                // Preview text when collapsed (first line of description)
                if (!isExpanded && item.description.isNotBlank()) {
                    Text(
                        text = item.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

/**
 * Header section of the help card containing icon, title, and expand button
 */
@Composable
private fun HelpCardHeader(
    title: String,
    isExpanded: Boolean,
    onExpandClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Icon and title section
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            // Help icon with background
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.secondaryContainer,
                shadowElevation = 2.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id=R.drawable.baseline_help),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Title text
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.semantics {
                    contentDescription = "Título de ayuda: $title"
                }
            )
        }

        // Expand/collapse button
        IconButton(
            onClick = onExpandClick,
            modifier = Modifier.semantics {
                contentDescription = if (isExpanded) "Contraer información" else "Expandir información"
            }
        ) {
            Icon(
                painter = if (isExpanded) painterResource(id=R.drawable.outline_expand_circle_up) else painterResource(id=R.drawable.outline_expand_circle_down),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.rotate(if (isExpanded) 0f else 0f)
            )
        }
    }
}

/**
 * Content section displaying the full description when expanded
 */
@Composable
private fun HelpCardContent(
    description: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Divider line
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Full description text
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight,
            modifier = Modifier.semantics {
                contentDescription = "Descripción detallada: $description"
            }
        )
    }
}
