package com.jsborbon.reparalo.components.comment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jsborbon.reparalo.ui.theme.RepairYellow

@Composable
fun CommentFormSection(
    commentText: MutableState<String>,
    rating: MutableState<Int>,
    onSubmit: () -> Unit,
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Comentarios",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Deja tu comentario",
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = commentText.value,
                    onValueChange = { commentText.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Escribe tu comentario aquí") },
                    minLines = 3,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Calificación:")

                Row {
                    repeat(5) { i ->
                        IconButton(onClick = { rating.value = i + 1 }) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Calificación ${i + 1}",
                                tint = if (i < rating.value) RepairYellow else Color.Gray.copy(alpha = 0.5f),
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        if (commentText.value.isNotBlank()) {
                            onSubmit()
                        }
                    },
                    modifier = Modifier.align(Alignment.End),
                ) {
                    Text(text = "Enviar comentario")
                }
            }
        }
    }
}
