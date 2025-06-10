package com.jsborbon.reparalo.screens.technician.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jsborbon.reparalo.R
import com.jsborbon.reparalo.components.InfoCard
import com.jsborbon.reparalo.components.InfoRow
import com.jsborbon.reparalo.screens.technician.components.cardComponents.ContactButtons
import kotlinx.coroutines.delay

/**
 * Component that displays technician specialties, availability, and contact options
 */
@Composable
fun TechnicianDetailsCard(
    modifier: Modifier = Modifier,
    specialties: List<String>,
    availability: String,
    phone: String,
    onSpecialtyClick: ((String) -> Unit)? = null,
) {
    val context = LocalContext.current
    val lazyListState = rememberLazyListState()

    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        isVisible = true
    }

    InfoCard(
        title = "Especialidades y Disponibilidad",
        modifier = modifier.semantics {
            contentDescription = "Información de especialidades y disponibilidad del técnico"
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Specialties Section
            if (specialties.isNotEmpty()) {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn() + slideInHorizontally { -it / 2 }
                ) {
                    Column {
                        Text(
                            text = "Especialidades",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .semantics {
                                    contentDescription = "Lista de especialidades del técnico"
                                }
                        )

                        LazyRow(
                            state = lazyListState,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(horizontal = 4.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(
                                items = specialties,
                                key = { specialty -> specialty }
                            ) { specialty ->
                                AnimatedVisibility(
                                    visible = isVisible,
                                    enter = fadeIn() + slideInHorizontally { it / 2 }
                                ) {
                                    AssistChip(
                                        onClick = {
                                            onSpecialtyClick?.invoke(specialty)
                                        },
                                        label = {
                                            Text(
                                                text = specialty,
                                                style = MaterialTheme.typography.labelMedium,
                                                fontWeight = FontWeight.Medium
                                            )
                                        },
                                        colors = AssistChipDefaults.assistChipColors(
                                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                            labelColor = MaterialTheme.colorScheme.onSecondaryContainer
                                        ),
                                        modifier = Modifier
                                            .semantics {
                                                role = Role.Button
                                                contentDescription = "Especialidad: $specialty. Toca para filtrar"
                                            }
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                Text(
                    text = "Sin especialidades definidas",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Availability Section
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn() + slideInHorizontally { -it / 2 }
            ) {
                Column {
                    Text(
                        text = "Disponibilidad",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    InfoRow(
                        icon = painterResource(id = R.drawable.baseline_schedule),
                        text = availability.ifBlank { "Disponibilidad no especificada" },
                        modifier = Modifier.semantics {
                            contentDescription = "Horario de disponibilidad: $availability"
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Contact Section
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn() + slideInHorizontally { it / 2 }
            ) {
                Column {
                    Text(
                        text = "Contacto",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    ContactButtons(
                        phone = phone,
                        context = context,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
