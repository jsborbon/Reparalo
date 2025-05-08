package com.jsborbon.reparalo.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsHelpScreen(navController: NavController) {
    val helpItems = remember { simulatedHelpItems }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Centro de Ayuda") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (helpItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("No hay contenido de ayuda disponible.")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                helpItems.forEach { item ->
                    HelpCard(item)
                }
            }
        }
    }
}

@Composable
fun HelpCard(item: HelpItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

data class HelpItem(
    val id: String,
    val title: String,
    val description: String
)

val simulatedHelpItems = listOf(
    HelpItem("1", "¿Cómo creo un tutorial?", "Ve a la sección de tutoriales y presiona el botón + para comenzar."),
    HelpItem("2", "¿Cómo contacto a un técnico?", "Busca en la sección de técnicos y haz clic en el perfil deseado."),
    HelpItem("3", "¿Dónde veo mis favoritos?", "Tus favoritos se encuentran en el perfil, sección 'Mis Favoritos'.")
)
