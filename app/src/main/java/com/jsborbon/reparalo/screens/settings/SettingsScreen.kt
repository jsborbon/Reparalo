package com.jsborbon.reparalo.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.jsborbon.reparalo.R
import com.jsborbon.reparalo.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
) {
    var isDarkTheme by remember { mutableStateOf(false) }
    var isNotificationsEnabled by remember { mutableStateOf(true) }
    var isLocationEnabled by remember { mutableStateOf(false) }
    var showInfoDialog by remember { mutableStateOf(false) }
    var showNotificationTypesDialog by remember { mutableStateOf(false) }

    var selectedNotificationTypes by remember { mutableStateOf(setOf<String>()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuración") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Atrás",
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
        ) {
            item {
                Text(
                    text = "Configuración de la aplicación",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 24.dp),
                )

                SettingsCategory(title = "Apariencia") {
                    SettingsSwitchItem(
                        icon = painterResource(id = R.drawable.baseline_dark_mode),
                        title = "Tema oscuro",
                        description = "Cambiar entre tema claro y oscuro",
                        checked = isDarkTheme,
                        onCheckedChange = { isDarkTheme = it },
                    )
                }

                SettingsCategory(title = "Notificaciones") {
                    SettingsSwitchItem(
                        icon = painterResource(id = R.drawable.baseline_notifications),
                        title = "Notificaciones",
                        description = "Recibir notificaciones de la aplicación",
                        checked = isNotificationsEnabled,
                        onCheckedChange = { isNotificationsEnabled = it },
                    )
                    SettingsClickableItem(
                        icon = painterResource(id = R.drawable.baseline_notifications_active),
                        title = "Tipos de notificaciones",
                        description = "Configurar qué notificaciones recibir",
                        onClick = { showNotificationTypesDialog = true },
                    )
                }

                SettingsCategory(title = "Privacidad") {
                    SettingsSwitchItem(
                        icon = painterResource(id = R.drawable.baseline_location_on),
                        title = "Ubicación",
                        description = "Permitir acceso a tu ubicación",
                        checked = isLocationEnabled,
                        onCheckedChange = { isLocationEnabled = it },
                    )
                    SettingsClickableItem(
                        icon = painterResource(id = R.drawable.baseline_security),
                        title = "Seguridad",
                        description = "Configurar opciones de seguridad",
                        onClick = { navController.navigate(Routes.SETTINGS_SECURITY) },
                    )
                }

                SettingsCategory(title = "Sobre la aplicación") {
                    SettingsClickableItem(
                        icon = painterResource(id = R.drawable.baseline_info),
                        title = "Acerca de",
                        description = "Versión 1.0.0",
                        onClick = { showInfoDialog = true },
                    )
                    SettingsClickableItem(
                        icon = painterResource(id = R.drawable.baseline_help),
                        title = "Ayuda",
                        description = "Preguntas frecuentes y soporte",
                        onClick = { navController.navigate(Routes.SETTINGS_HELP) },
                    )
                    SettingsClickableItem(
                        icon = painterResource(id = R.drawable.baseline_policy),
                        title = "Términos y condiciones",
                        description = "Políticas de privacidad y uso",
                        onClick = { navController.navigate(Routes.SETTINGS_TERMS) },
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        FirebaseAuth.getInstance().signOut()
                        navController.navigate(Routes.AUTHENTICATION) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                    ),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Cerrar sesión",
                        modifier = Modifier.padding(end = 8.dp),
                    )
                    Text("Cerrar sesión")
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Reparalo v1.0.0",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }

    if (showInfoDialog) {
        AlertDialog(
            onDismissRequest = { showInfoDialog = false },
            confirmButton = {
                TextButton(onClick = { showInfoDialog = false }) {
                    Text("Aceptar")
                }
            },
            title = { Text("Acerca de Reparalo") },
            text = {
                Text("Aplicación para gestionar tutoriales de reparación. Versión 1.0.0")
            },
        )
    }

    if (showNotificationTypesDialog) {
        NotificationTypesDialog(
            onDismiss = { showNotificationTypesDialog = false },
            selectedTypes = selectedNotificationTypes,
            onToggleType = { type ->
                selectedNotificationTypes = if (selectedNotificationTypes.contains(type)) {
                    selectedNotificationTypes - type
                } else {
                    selectedNotificationTypes + type
                }
            },
        )
    }
}

@Composable
fun NotificationTypesDialog(
    onDismiss: () -> Unit,
    selectedTypes: Set<String>,
    onToggleType: (String) -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Aceptar")
            }
        },
        title = { Text("Tipos de notificaciones") },
        text = {
            Column {
                val types = listOf("Promociones", "Novedades", "Alertas técnicas")
                types.forEach { type ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onToggleType(type) }
                            .padding(vertical = 8.dp),
                    ) {
                        Checkbox(
                            checked = selectedTypes.contains(type),
                            onCheckedChange = { onToggleType(type) },
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = type)
                    }
                }
            }
        },
    )
}
