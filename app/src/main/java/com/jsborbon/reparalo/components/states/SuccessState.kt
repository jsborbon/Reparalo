package com.jsborbon.reparalo.components.states

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Generic success state to render dynamic content or an empty fallback.
 *
 * @param modifier Layout modifier.
 * @param hasData Whether there's content to show.
 * @param content Composable content to render when data exists.
 * @param emptyMessage Message to show when there's no data.
 * @param semanticDescription Accessibility label for screen readers.
 */
@Composable
fun SuccessState(
    hasData: Boolean,
    modifier: Modifier = Modifier,
    emptyMessage: String = "No hay datos para mostrar",
    semanticDescription: String = "Estado de éxito",
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .semantics { contentDescription = semanticDescription },
        contentAlignment = Alignment.Center
    ) {
        if (hasData) {
            content()
        } else {
            Text(
                text = emptyMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}
