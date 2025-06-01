package com.jsborbon.reparalo.screens.technician.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jsborbon.reparalo.R
import com.jsborbon.reparalo.components.InfoCard
import com.jsborbon.reparalo.components.InfoRow

@Composable
fun TechnicianDetailsCard(
    specialties: List<String>,
    availability: String,
    onSpecialtyClick: ((String) -> Unit)? = null,
) {
    InfoCard(title = "Especialidades") {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(specialties) { specialty ->
                AssistChip(
                    onClick = { onSpecialtyClick?.invoke(specialty) },
                    label = { Text(specialty) },
                    colors = AssistChipDefaults.assistChipColors(),
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Disponibilidad",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
        )

        InfoRow(
            icon = painterResource(id = R.drawable.baseline_schedule),
            text = availability,
        )
    }
}
