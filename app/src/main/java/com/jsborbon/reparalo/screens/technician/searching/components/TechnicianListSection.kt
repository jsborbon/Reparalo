package com.jsborbon.reparalo.screens.technician.searching.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jsborbon.reparalo.components.states.EmptyState
import com.jsborbon.reparalo.models.User
import com.jsborbon.reparalo.screens.technician.components.TechnicianCard
import java.util.Locale

/**
 * Displays a scrollable, animated, and accessible list of technicians.
 * Includes title section, staggered item animations, and fallback empty state.
 */
@Composable
fun TechnicianListSection(
    modifier: Modifier = Modifier,
    technicians: List<User>,
    lazyListState: LazyListState,
    showTitle: Boolean = true,
    titleText: String = "Conecta con Profesionales",
    onTechnicianClick: ((User) -> Unit)? = null,
) {
    // Descriptive text for screen reader support based on list size
    val technicianCountText = remember(technicians.size) {
        when (technicians.size) {
            0 -> "No hay técnicos disponibles"
            1 -> "1 técnico disponible"
            else -> "${technicians.size} técnicos disponibles"
        }
    }

    // Show empty state if list is empty
    if (technicians.isEmpty()) {
        EmptyState(
            modifier = modifier.fillMaxSize(),
            isSearching = false,
            searchQuery = "",
            onClearSearch = null
        )
    } else {
        LazyColumn(
            state = lazyListState,
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp,
                bottom = 100.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
                .fillMaxSize()
                .semantics {
                    contentDescription = "$technicianCountText en la lista"
                }
        ) {
            if (showTitle) {
                item(key = "title_section") {
                    Column(
                        modifier = modifier.padding(vertical = 8.dp)
                            .fillMaxWidth()
                            .clearAndSetSemantics {
                                contentDescription = "$titleText. $technicianCountText"
                            }
                    ) {
                        Text(
                            text = titleText,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(4.dp).padding(vertical = 8.dp))

                        Text(
                            text = technicianCountText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Animated list of technician cards
            itemsIndexed(
                items = technicians,
                key = { _, technician -> technician.uid }
            ) { index, technician ->
                val animationDelay = (index * 75).coerceAtMost(300)

                AnimatedVisibility(
                    visible = true,
                    enter = slideInVertically(
                        initialOffsetY = { fullHeight -> fullHeight / 3 },
                        animationSpec = tween(
                            durationMillis = 400,
                            delayMillis = animationDelay,
                            easing = LinearOutSlowInEasing
                        )
                    ) + fadeIn(
                        animationSpec = tween(
                            durationMillis = 300,
                            delayMillis = animationDelay
                        )
                    ),
                    label = "technician_${technician.uid}"
                ) {
                    TechnicianCard(
                        technician = technician,
                        onClick = { onTechnicianClick?.invoke(technician) },
                        padding = PaddingValues(0.dp),
                        modifier = Modifier
                            .semantics {
                                contentDescription = buildTechnicianAccessibilityText(
                                    technician,
                                    index + 1,
                                    technicians.size
                                )
                            }
                    )
                }
            }

            item(key = "bottom_spacer") {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

/**
 * Returns an accessibility-friendly description for a technician card.
 */
private fun buildTechnicianAccessibilityText(
    technician: User,
    position: Int,
    totalCount: Int
): String {
    val specialty = technician.specialty?.takeIf { it.isNotBlank() } ?: "servicios generales"
    val ratingText = if (technician.rating > 0) {
        ", calificación ${String.format(Locale.getDefault(), "%.1f", technician.rating)} de 5 estrellas"
    } else {
        ", sin calificaciones aún"
    }

    return "Técnico $position de $totalCount. ${technician.name}, especialista en $specialty$ratingText. Presiona para ver más detalles."
}
