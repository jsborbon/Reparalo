package com.jsborbon.reparalo.screens.technician.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri

@Composable
fun ContactButtons(phone: String, context: Context) {
    if (phone.isNotBlank()) {
        val cleanedPhone = phone.trim().replace(" ", "")

        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, "https://wa.me/$cleanedPhone".toUri())
                context.startActivity(intent)
            },
            modifier = Modifier.padding(top = 16.dp),
        ) {
            Text("Contactar por WhatsApp")
        }

        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_DIAL, "tel:$cleanedPhone".toUri())
                context.startActivity(intent)
            },
            modifier = Modifier.padding(top = 8.dp),
        ) {
            Text("Llamar")
        }
    } else {
        Text(
            text = "Número de teléfono no disponible",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 16.dp),
        )
    }
}
