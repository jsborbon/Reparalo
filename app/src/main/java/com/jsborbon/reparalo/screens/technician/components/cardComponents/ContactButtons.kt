package com.jsborbon.reparalo.screens.technician.components.cardComponents

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jsborbon.reparalo.screens.technician.components.cardComponents.contactButtons.CallButton
import com.jsborbon.reparalo.screens.technician.components.cardComponents.contactButtons.SMSButton
import com.jsborbon.reparalo.screens.technician.components.cardComponents.contactButtons.WhatsAppButton
import kotlinx.coroutines.delay

@Composable
fun ContactButtons(
    phone: String,
    context: Context,
    modifier: Modifier = Modifier,
    showWhatsApp: Boolean = true,
    showCall: Boolean = true,
    showSMS: Boolean = false,
) {
    val isVisible = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(200)
        isVisible.value = true
    }

    AnimatedVisibility(
        visible = isVisible.value,
        enter = fadeIn() + slideInVertically { it / 2 }
    ) {
        if (phone.isNotBlank()) {
            ContactActionsAvailable(
                phone = phone,
                context = context,
                modifier = modifier,
                showWhatsApp = showWhatsApp,
                showCall = showCall,
                showSMS = showSMS
            )
        } else {
            ContactUnavailableState(modifier = modifier)
        }
    }
}

@Composable
private fun ContactActionsAvailable(
    phone: String,
    context: Context,
    modifier: Modifier = Modifier,
    showWhatsApp: Boolean,
    showCall: Boolean,
    showSMS: Boolean,
) {
    val cleanedPhone = remember(phone) {
        phone.trim().replace(Regex("[^+\\d]"), "")
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Opciones de contacto",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (showWhatsApp) {
                WhatsAppButton(
                    phone = cleanedPhone,
                    context = context,
                    modifier = Modifier.weight(1f)
                )
            }

            if (showCall) {
                CallButton(
                    phone = cleanedPhone,
                    context = context,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        if (showSMS) {
            SMSButton(
                phone = cleanedPhone,
                context = context,
                modifier = Modifier.fillMaxWidth()
            )
        }

        PhoneNumberDisplay(phone = cleanedPhone)
    }
}

@Composable
private fun PhoneNumberDisplay(phone: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Phone,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = formatPhoneNumber(phone),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.semantics {
                    contentDescription = "Número de teléfono: $phone"
                }
            )
        }
    }
}

@Composable
private fun ContactUnavailableState(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Información de contacto no disponible",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.semantics {
                    contentDescription = "El usuario no ha proporcionado información de contacto"
                }
            )
            Text(
                text = "No se ha proporcionado un número de teléfono",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

private fun formatPhoneNumber(phone: String): String {
    return when {
        phone.startsWith("+") -> phone
        phone.length >= 9 -> {
            val cleaned = phone.replace(Regex("\\D"), "")
            when (cleaned.length) {
                9 -> "${cleaned.substring(0, 3)} ${cleaned.substring(3, 6)} ${cleaned.substring(6)}"
                10 -> "${cleaned.substring(0, 3)} ${cleaned.substring(3, 6)} ${cleaned.substring(6)}"
                else -> phone
            }
        }
        else -> phone
    }
}
