package com.jsborbon.reparalo.screens.technician.components

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.jsborbon.reparalo.R
import com.jsborbon.reparalo.components.InfoCard
import com.jsborbon.reparalo.components.InfoRow
import com.jsborbon.reparalo.models.User
import com.jsborbon.reparalo.ui.theme.RepairYellow
import com.jsborbon.reparalo.utils.formatDate
import java.util.Locale

@Composable
fun TechnicianCard(
    technician: User,
    onClick: (() -> Unit)? = null,
    padding: PaddingValues = PaddingValues(0.dp),
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(padding)
            .clickable(enabled = onClick != null) { onClick?.invoke() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = technician.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = RepairYellow,
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = String.format(Locale.getDefault(), "%.1f", technician.rating),
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }

            technician.specialty?.takeIf { it.isNotBlank() }?.let {
                Text(
                    text = "Especialidad: $it",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }

            if (technician.availability.isNotBlank()) {
                Text(
                    text = "Disponibilidad: ${technician.availability}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            InfoCard(title = "Información de contacto") {
                InfoRow(icon = painterResource(id = R.drawable.baseline_email), text = technician.email)
                if (technician.phone.isNotBlank()) {
                    InfoRow(icon = painterResource(id = R.drawable.baseline_phone), text = technician.phone)
                }
                InfoRow(
                    icon = painterResource(id = R.drawable.baseline_date_range),
                    text = "Miembro desde: ${formatDate(technician.registrationDate)}",
                )
            }

            if (technician.phone.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Button(
                        onClick = {
                            val phone = technician.phone.trim().replace(" ", "")
                            val intent = Intent(Intent.ACTION_VIEW, "https://wa.me/$phone".toUri())
                            context.startActivity(intent)
                        },
                    ) {
                        Text("WhatsApp")
                    }
                    Button(
                        onClick = {
                            val phone = technician.phone.trim().replace(" ", "")
                            val intent = Intent(Intent.ACTION_DIAL, "tel:$phone".toUri())
                            context.startActivity(intent)
                        },
                    ) {
                        Text("Llamar")
                    }
                }
            }

            if (technician.email.isNotBlank()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.End,
                ) {
                    IconButton(onClick = {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = "mailto:${technician.email}".toUri()
                        }
                        context.startActivity(intent)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Contactar",
                        )
                    }
                }
            }
        }
    }
}
