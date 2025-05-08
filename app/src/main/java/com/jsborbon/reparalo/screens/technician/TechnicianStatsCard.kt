package com.jsborbon.reparalo.screens.technician

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jsborbon.reparalo.components.InfoCard
import com.jsborbon.reparalo.components.StatisticItem

@Composable
fun TechnicianStatsCard(completedServices: Int, rating: Float) {
    InfoCard(title = "Estadísticas") {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            StatisticItem(icon = Icons.Default.Build, value = completedServices.toString(), label = "Servicios")
            StatisticItem(icon = Icons.Default.Star, value = rating.toString(), label = "Calificación")
            StatisticItem(icon = Icons.Default.ThumbUp, value = "92%", label = "Satisfacción")
        }
    }
}
