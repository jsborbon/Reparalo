package com.jsborbon.reparalo.screens.dashboard.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jsborbon.reparalo.navigation.Routes

@Composable
fun CategorySection(navController: NavController) {
    Row(modifier = Modifier.fillMaxWidth()) {
        CategoryCard(
            title = "Tutoriales",
            icon = Icons.Default.Star,
            backgroundColor = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.weight(1f),
            onClick = { navController.navigate(Routes.TUTORIALS) }
        )
        Spacer(modifier = Modifier.width(16.dp))
        CategoryCard(
            title = "Materiales",
            icon = Icons.Default.Build,
            backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier.weight(1f),
            onClick = { navController.navigate(Routes.MATERIALS_LIST) }
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    Row(modifier = Modifier.fillMaxWidth()) {
        CategoryCard(
            title = "Profesionales",
            icon = Icons.Default.Person,
            backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
            modifier = Modifier.weight(1f),
            onClick = { navController.navigate(Routes.PROFESSIONAL_CONNECTION) }
        )
        Spacer(modifier = Modifier.width(16.dp))
        CategoryCard(
            title = "Foro",
            icon = Icons.Default.Info,
            backgroundColor = Color(0xFFE1F5FE),
            modifier = Modifier.weight(1f),
            onClick = { navController.navigate(Routes.FORUM) }
        )
    }

    Spacer(modifier = Modifier.height(24.dp))
}
