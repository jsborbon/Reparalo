package com.jsborbon.reparalo.screens.technician.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jsborbon.reparalo.models.User
import com.jsborbon.reparalo.screens.technician.components.cardComponents.StatisticItem
import com.jsborbon.reparalo.ui.theme.Blue40
import com.jsborbon.reparalo.ui.theme.BlueGrey40
import com.jsborbon.reparalo.ui.theme.RepairYellow
import com.jsborbon.reparalo.ui.theme.Success
import java.util.Locale

@Composable
fun TechnicianInfoCard(technician: User) {
    val rating = technician.rating
    val satisfaction = technician.satisfaction
    val completedServices = technician.completedServices

    val trustScore = calculateTrustScore(completedServices, rating, satisfaction)
    val (trustLabel, trustColor) = getTrustLevelInfo(trustScore)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .semantics {
                contentDescription = "Perfil de ${technician.name}, $completedServices servicios, $rating estrellas, ${(satisfaction * 100).toInt()}% satisfacción"
            },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Perfil de ${technician.name}",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center
            )
            technician.specialty?.let {
                Text(
                    text = "Especialista en $it",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatisticItem(
                    icon = Icons.Default.Build,
                    label = "Servicios",
                    subtitle = "completados",
                    value = completedServices.toString(),
                    iconTint = MaterialTheme.colorScheme.primary,
                    animationDelay = 0L
                )
                VerticalDivider()
                StatisticItem(
                    icon = Icons.Default.Star,
                    label = "Calificación",
                    subtitle = "promedio",
                    value = String.format(Locale.getDefault(), "%.1f", rating),
                    iconTint = RepairYellow,
                    showStars = true,
                    starsValue = rating,
                    animationDelay = 100L
                )
                VerticalDivider()
                StatisticItem(
                    icon = Icons.Default.ThumbUp,
                    label = "Satisfacción",
                    subtitle = "del cliente",
                    value = "${(satisfaction * 100).toInt()}%",
                    iconTint = Success,
                    showProgressBar = true,
                    progressValue = satisfaction,
                    animationDelay = 200L
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(trustColor.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Nivel de confianza:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    trustLabel,
                    fontWeight = FontWeight.Bold,
                    color = trustColor,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

private fun calculateTrustScore(services: Int, rating: Float, satisfaction: Float): Float {
    val sWeight = (services.coerceAtMost(50) / 50f) * 0.3f
    val rWeight = (rating / 5f) * 0.4f
    val satWeight = satisfaction * 0.3f
    return sWeight + rWeight + satWeight
}

private fun getTrustLevelInfo(score: Float): Pair<String, Color> = when {
    score >= 0.8f -> "Excelente" to Success
    score >= 0.6f -> "Muy bueno" to RepairYellow
    score >= 0.4f -> "Bueno" to Blue40
    else -> "En desarrollo" to BlueGrey40
}

@Composable
private fun VerticalDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(60.dp)
            .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
    )
}
