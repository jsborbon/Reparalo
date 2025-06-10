package com.jsborbon.reparalo.screens.technician.components.cardComponents.contactButtons

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.jsborbon.reparalo.R


@Composable
fun SMSButton(
    phone: String,
    context: Context,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        onClick = {
            try {
                val intent = Intent(Intent.ACTION_SENDTO, "smsto:$phone".toUri()).apply {
                    putExtra("sms_body", "Hola, estoy interesado en sus servicios técnicos.")
                }
                context.startActivity(intent)
            } catch (_: Exception) {
                Toast.makeText(
                    context,
                    "No se pudo abrir la aplicación de mensajes",
                    Toast.LENGTH_SHORT
                ).show()
            }
        },
        modifier = modifier.semantics {
            role = Role.Button
            contentDescription = "Enviar mensaje de texto al número $phone"
        }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_email), // Using placeholder icon
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Enviar SMS",
            style = MaterialTheme.typography.labelLarge
        )
    }
}
