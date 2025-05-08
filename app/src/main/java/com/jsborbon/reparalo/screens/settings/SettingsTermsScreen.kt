package com.jsborbon.reparalo.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTermsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Términos y Condiciones") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Última actualización: 06/05/2025",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "1. Uso de la aplicación",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Esta aplicación tiene como propósito brindar asistencia en reparaciones domésticas mediante tutoriales, materiales recomendados y contacto con profesionales técnicos."
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "2. Privacidad y datos",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Tu información será tratada con confidencialidad y conforme a la legislación vigente. No compartimos tus datos sin consentimiento explícito."
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "3. Responsabilidad del contenido",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Los tutoriales son de carácter informativo. El usuario es responsable de aplicar correctamente las indicaciones y tomar medidas de seguridad adecuadas."
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Al continuar usando la app, aceptas estos términos.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
