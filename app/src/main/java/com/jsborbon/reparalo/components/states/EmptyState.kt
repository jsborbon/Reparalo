package com.jsborbon.reparalo.components.states

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jsborbon.reparalo.R

/**
 * Displays a generic empty state with support for search context and action.
 *
 * @param isSearching Indicates if a search is being performed.
 * @param searchQuery The current search query to display (if any).
 * @param onClearSearch Optional callback to reset the search query.
 */
@Composable
fun EmptyState(
    modifier: Modifier = Modifier,
    isSearching: Boolean,
    searchQuery: String = "",
    onClearSearch: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp)
            .semantics { contentDescription = "Estado vacío" },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_help),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = when {
                isSearching -> "Sin resultados"
                else -> "Contenido no disponible"
            },
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = when {
                isSearching -> "No se encontraron resultados para \"$searchQuery\""
                else -> "No hay contenido disponible en este momento."
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        if (isSearching && onClearSearch != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onClearSearch) {
                Text("Limpiar búsqueda")
            }
        }
    }
}
