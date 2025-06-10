package com.jsborbon.reparalo.screens.tutorial.components

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jsborbon.reparalo.R
import com.jsborbon.reparalo.models.Tutorial
import kotlinx.coroutines.delay

@Composable
fun TutorialHeaderSection(
    tutorial: Tutorial,
    tutorialId: String,
    favorites: Set<String>,
    onToggleFavorite: () -> Unit,
) {
    val context = LocalContext.current
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(tutorial.id) {
        delay(100)
        visible = true
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(300)) + expandVertically(tween(400))
            ) {
                Column {
                    Text(
                        text = tutorial.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Por: ${tutorial.author.name}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.outline_category),
                                contentDescription = "Categoría",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Text(
                                text = tutorial.category,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.outline_timer_arrow_up),
                                contentDescription = "Duración estimada",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Text(
                                text = tutorial.estimatedDuration,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        Text(
                            text = "Nivel: ${tutorial.difficultyLevel}",
                            style = MaterialTheme.typography.bodySmall,
                            color = getDifficultyColor(tutorial.difficultyLevel)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = tutorial.description,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ToggleFavoriteButton(
                            tutorialId = tutorialId,
                            favorites = favorites,
                            onToggle = onToggleFavorite
                        )

                        Button(
                            onClick = {
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_SUBJECT, tutorial.title)
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        "¡Mira este tutorial en Repáralo! 🔧\n\n" +
                                            "${tutorial.title}\n\n" +
                                            "${tutorial.description}\n\n" +
                                            "Duración: ${tutorial.estimatedDuration} | " +
                                            "Dificultad: ${tutorial.difficultyLevel}"
                                    )
                                }
                                context.startActivity(Intent.createChooser(shareIntent, "Compartir tutorial"))
                            },
                            modifier = Modifier.semantics {
                                contentDescription = "Botón para compartir tutorial"
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = null,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text("Compartir")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun getDifficultyColor(difficultyLevel: String): Color {
    return when (difficultyLevel.lowercase()) {
        "fácil" -> MaterialTheme.colorScheme.tertiary
        "medio" -> MaterialTheme.colorScheme.secondary
        "difícil" -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
}
