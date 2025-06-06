package com.jsborbon.reparalo.screens.profile.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.jsborbon.reparalo.R
import com.jsborbon.reparalo.components.InfoCard
import com.jsborbon.reparalo.components.InfoRow

@Composable
fun ContactInfoCard(email: String, phone: String, joinDate: String) {
    InfoCard(title = "Información de contacto") {
        InfoRow(icon = painterResource(id = R.drawable.baseline_email), text = email)
        InfoRow(icon = painterResource(id = R.drawable.baseline_phone), text = phone)
        InfoRow(icon = painterResource(id = R.drawable.baseline_date_range), text = "Miembro desde: $joinDate")
    }
}
