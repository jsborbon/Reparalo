package com.jsborbon.reparalo.screens.technician.components.cardComponents.contactButtons

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.jsborbon.reparalo.R


@Composable
fun WhatsAppButton(
    phone: String,
    context: Context,
    modifier: Modifier = Modifier,
) {
    FilledTonalButton(
        onClick = {
            try {
                val intent = Intent(Intent.ACTION_VIEW, "https://wa.me/$phone".toUri())
                context.startActivity(intent)
            } catch (_: Exception) {
                Toast.makeText(
                    context,
                    "WhatsApp no está disponible en este dispositivo",
                    Toast.LENGTH_SHORT
                ).show()
            }
        },
        modifier = modifier.semantics {
            role = Role.Button
            contentDescription = "Contactar por WhatsApp al número $phone"
        },
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_phone), // Using placeholder icon
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "WhatsApp",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium
        )
    }
}
