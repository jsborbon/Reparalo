package com.jsborbon.reparalo.components.comment

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jsborbon.reparalo.ui.theme.RepairYellow

@Composable
fun CommentFormSection(
    commentText: MutableState<String>,
    rating: MutableState<Int>,
    onSubmit: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val isFormValid = commentText.value.isNotBlank() && rating.value > 0

    // Animation for button when form becomes valid
    val buttonScale by animateFloatAsState(
        targetValue = if (isFormValid) 1f else 0.95f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "buttonScale"
    )

    Column(
        modifier = Modifier
            .padding(16.dp)
            .semantics {
                contentDescription = "Sección para escribir comentarios y calificaciones"
            }
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Enhanced section title with better typography
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Surface(
                modifier = Modifier.size(4.dp),
                shape = RoundedCornerShape(2.dp),
                color = MaterialTheme.colorScheme.primary
            ) {}
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Comentarios",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Enhanced form card with better visual hierarchy
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentDescription = "Formulario para escribir comentario"
                },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                // Enhanced form header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "✍️",
                        fontSize = 20.sp,
                        modifier = Modifier.semantics {
                            contentDescription = "Icono de escribir"
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Deja tu comentario",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Enhanced text field with better styling
                OutlinedTextField(
                    value = commentText.value,
                    onValueChange = { commentText.value = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics {
                            contentDescription = "Campo de texto para escribir comentario"
                        },
                    placeholder = {
                        Text(
                            "Comparte tu experiencia con este tutorial...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    },
                    minLines = 3,
                    maxLines = 6,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    ),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        lineHeight = 20.sp
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Enhanced rating section with better visual feedback
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "⭐",
                                fontSize = 18.sp,
                                modifier = Modifier.semantics {
                                    contentDescription = "Icono de calificación"
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Calificación:",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            // Display current rating
                            if (rating.value > 0) {
                                Text(
                                    text = "${rating.value}/5",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Enhanced star rating with better interaction
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .semantics {
                                    contentDescription = "Selecciona calificación del 1 al 5"
                                }
                        ) {
                            repeat(5) { index ->
                                val isSelected = index < rating.value
                                val starScale by animateFloatAsState(
                                    targetValue = if (isSelected) 1.1f else 1f,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessHigh
                                    ),
                                    label = "starScale$index"
                                )

                                Surface(
                                    modifier = Modifier
                                        .size(48.dp) // Minimum touch target
                                        .scale(starScale)
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null
                                        ) { rating.value = index + 1 },
                                    shape = RoundedCornerShape(24.dp),
                                    color = if (isSelected)
                                        RepairYellow.copy(alpha = 0.2f)
                                    else
                                        Color.Transparent
                                ) {
                                    Icon(
                                        imageVector = if (isSelected)
                                            Icons.Filled.Star
                                        else
                                            Icons.Outlined.Star,
                                        contentDescription = "Calificación ${index + 1} estrella${if (index > 0) "s" else ""}",
                                        tint = if (isSelected)
                                            RepairYellow
                                        else
                                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                        modifier = Modifier
                                            .size(28.dp)
                                            .padding(10.dp)
                                    )
                                }
                            }
                        }

                        // Helpful text for rating
                        AnimatedVisibility(
                            visible = rating.value > 0,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            Column {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = when (rating.value) {
                                        1 -> "Muy malo"
                                        2 -> "Malo"
                                        3 -> "Regular"
                                        4 -> "Bueno"
                                        5 -> "Excelente"
                                        else -> ""
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Medium,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Enhanced submit button with better feedback
                Button(
                    onClick = {
                        if (isFormValid) {
                            focusManager.clearFocus()
                            onSubmit()
                        }
                    },
                    enabled = isFormValid,
                    modifier = Modifier
                        .align(Alignment.End)
                        .scale(buttonScale)
                        .semantics {
                            contentDescription = if (isFormValid)
                                "Enviar comentario"
                            else
                                "Complete el comentario y calificación para enviar"
                        },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = if (isFormValid) 6.dp else 0.dp,
                        pressedElevation = 2.dp
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Enviar comentario",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
