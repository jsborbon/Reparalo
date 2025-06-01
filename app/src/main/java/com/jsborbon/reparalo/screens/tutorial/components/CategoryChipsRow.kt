package com.jsborbon.reparalo.screens.tutorial.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jsborbon.reparalo.models.Category

@Composable
fun CategoryChipsRow(
    categories: List<Category>,
    selectedCategory: String?,
    onCategorySelected: (Category) -> Unit,
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        items(categories) { category ->
            val isSelected = selectedCategory == category.name
            FilterChip(
                selected = isSelected,
                onClick = { onCategorySelected(category) },
                label = { Text(category.name) },
                modifier = Modifier.padding(end = 8.dp),
            )
        }
    }
}
