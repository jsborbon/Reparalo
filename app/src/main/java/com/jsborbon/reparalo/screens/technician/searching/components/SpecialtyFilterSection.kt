package com.jsborbon.reparalo.screens.technician.searching.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jsborbon.reparalo.models.Category

/**
 * Specialty filter chips section with improved layout and accessibility
 */
@Composable
fun SpecialtyFilterSection(
    specialties: List<Category>,
    selectedSpecialty: String?,
    onSpecialtySelected: (Category) -> Unit
) {
    Column {
        Text(
            text = "Especialidades",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            items(specialties, key = { it.id }) { category ->
                val isSelected = selectedSpecialty == category.name ||
                    (selectedSpecialty == null && category.name == "Todos")

                FilterChip(
                    selected = isSelected,
                    onClick = { onSpecialtySelected(category) },
                    label = {
                        Text(
                            text = category.name,
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    modifier = Modifier.semantics {
                        contentDescription = if (isSelected) {
                            "Especialidad ${category.name} seleccionada"
                        } else {
                            "Seleccionar especialidad ${category.name}"
                        }
                    }
                )
            }
        }
    }
}
