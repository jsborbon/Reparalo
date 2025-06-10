package com.jsborbon.reparalo.screens.tutorial.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jsborbon.reparalo.models.Category

@Composable
fun CategoryChipsRow(
    categories: List<Category>,
    selectedCategoryId: String?,
    onCategorySelected: (Category) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    val rowDescription = remember(categories, selectedCategoryId) {
        val selection = selectedCategoryId?.let { id ->
            categories.find { it.id == id }?.name ?: "ninguna"
        } ?: "ninguna"
        "Filtros de categoría. ${categories.size} opciones. Selección actual: $selection"
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .padding(vertical = 4.dp)
    ) {
        if (categories.isEmpty()) {
            Text(
                text = "No hay categorías disponibles",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            LazyRow(
                state = listState,
                contentPadding = PaddingValues(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics {
                        contentDescription = rowDescription
                    }
                    .testTag("category_chips_row")
            ) {
                items(
                    items = categories,
                    key = { it.id }
                ) { category ->
                    CategoryChip(
                        category = category,
                        isSelected = selectedCategoryId == category.id,
                        onSelected = { onCategorySelected(category) },
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryChip(
    category: Category,
    isSelected: Boolean,
    onSelected: () -> Unit,
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = tween(durationMillis = 200),
        label = "chipScale"
    )

    val containerColor by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        },
        animationSpec = tween(durationMillis = 200),
        label = "chipContainerColor"
    )

    val labelColor by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.onPrimaryContainer
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        animationSpec = tween(durationMillis = 200),
        label = "chipLabelColor"
    )

    val borderColor = when {
        isSelected -> MaterialTheme.colorScheme.primary
        else -> Color.Transparent
    }

    val borderWidth = if (isSelected) 2.dp else 0.dp

    val chipDescription = remember(isSelected) {
        "Categoría ${category.name}. ${if (isSelected) "Seleccionada" else "No seleccionada"}. Presiona para ${if (isSelected) "deseleccionar" else "seleccionar"}."
    }

    FilterChip(
        selected = isSelected,
        onClick = onSelected,
        modifier = Modifier
            .scale(scale)
            .testTag("category_chip_${category.id}")
            .semantics { contentDescription = chipDescription }
            .border(
                width = borderWidth,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            ),
        label = {
            Text(
                text = category.name,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = labelColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = containerColor,
            containerColor = containerColor,
            selectedLabelColor = labelColor,
            labelColor = labelColor
        )
    )
}
