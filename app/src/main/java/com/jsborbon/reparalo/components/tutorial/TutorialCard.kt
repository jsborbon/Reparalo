package com.jsborbon.reparalo.components.tutorial

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jsborbon.reparalo.models.Tutorial

@Composable
fun TutorialCard(
    tutorial: Tutorial,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onDeleteClick: (() -> Unit)? = null,
    onItemClick: () -> Unit,
    currentUserId: String? = null,
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClick),
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = tutorial.title,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = tutorial.description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 2.dp, bottom = 8.dp),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Autor: ${tutorial.author.name}",
                    style = MaterialTheme.typography.labelMedium,
                )
                Row {
                    IconButton(onClick = onFavoriteClick) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Favorito",
                            tint = if (isFavorite) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            },
                        )
                    }

                    if (tutorial.author.uid == currentUserId && onDeleteClick != null) {
                        IconButton(onClick = onDeleteClick) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar",
                                tint = MaterialTheme.colorScheme.error,
                            )
                        }
                    }
                }
            }
        }
    }
}
