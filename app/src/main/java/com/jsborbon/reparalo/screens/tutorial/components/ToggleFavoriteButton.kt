package com.jsborbon.reparalo.screens.tutorial.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jsborbon.reparalo.R
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ToggleFavoriteButton(
    tutorialId: String,
    favorites: StateFlow<Set<String>>,
    onToggle: () -> Unit,
) {
    val favoriteList = favorites.collectAsState().value
    val isFavorite = tutorialId in favoriteList

    IconButton(onClick = onToggle) {
        Icon(
            painter = painterResource(
                id = if (isFavorite) R.drawable.baseline_favorite else R.drawable.outline_favorite,
            ),
            contentDescription = if (isFavorite) "Eliminar de favoritos" else "Añadir a favoritos",
            tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(24.dp),
        )
    }
}
