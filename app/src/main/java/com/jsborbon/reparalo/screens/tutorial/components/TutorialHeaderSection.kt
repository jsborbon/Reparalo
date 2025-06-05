package com.jsborbon.reparalo.screens.tutorial.components

import ToggleFavoriteButton
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jsborbon.reparalo.models.Tutorial

@Composable
fun TutorialHeaderSection(
    tutorial: Tutorial,
    tutorialId: String,
    favorites: Set<String>,
    onToggleFavorite: () -> Unit,
) {
    val context = LocalContext.current

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = tutorial.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "Autor: ${tutorial.author.name}",
            style = MaterialTheme.typography.bodyMedium,
        )
        Text(
            text = tutorial.description,
            style = MaterialTheme.typography.bodyLarge,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            ToggleFavoriteButton(
                tutorialId = tutorialId,
                favorites = favorites,
                onToggle = onToggleFavorite,
            )
            Button(
                onClick = {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_SUBJECT, tutorial.title)
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "¡Mira este tutorial! ${tutorial.title} - ${tutorial.description}",
                        )
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Compartir vía"))
                },
            ) {
                Text("Compartir")
            }
        }
    }
}
