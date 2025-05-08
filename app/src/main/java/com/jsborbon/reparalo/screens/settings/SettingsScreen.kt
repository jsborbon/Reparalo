package com.jsborbon.reparalo.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.jsborbon.reparalo.R
import com.jsborbon.reparalo.navigation.Routes
import com.jsborbon.reparalo.viewmodels.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = viewModel()
) {
    val context = LocalContext.current

    var isDarkTheme by remember { mutableStateOf(false) }
    var isNotificationsEnabled by remember { mutableStateOf(true) }
    var isLocationEnabled by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showInfoDialog by remember { mutableStateOf(false) }
    var showNotificationTypesDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuración") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Atrás"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            item {
                Text(
                    text = "Configuración de la aplicación",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                SettingsCategory(title = "Apariencia") {
                    SettingsSwitchItem(
                        icon = painterResource(id = R.drawable.baseline_dark_mode),
                        title = "Tema oscuro",
                        description = "Cambiar entre tema claro y oscuro",
                        checked = isDarkTheme,
                        onCheckedChange = { isDarkTheme = it }
                    )
                }

                SettingsCategory(title = "Notificaciones") {
                    SettingsSwitchItem(
                        icon = painterResource(id = R.drawable.baseline_notifications),
                        title = "Notificaciones",
                        description = "Recibir notificaciones de la aplicación",
                        checked = isNotificationsEnabled,
                        onCheckedChange = { isNotificationsEnabled = it }
                    )
                    SettingsClickableItem(
                        icon = painterResource(id = R.drawable.baseline_notifications_active),
                        title = "Tipos de notificaciones",
                        description = "Configurar qué notificaciones recibir",
                        onClick = { showNotificationTypesDialog = true }
                    )
                }

                SettingsCategory(title = "Privacidad") {
                    SettingsSwitchItem(
                        icon = painterResource(id = R.drawable.baseline_location_on),
                        title = "Ubicación",
                        description = "Permitir acceso a tu ubicación",
                        checked = isLocationEnabled,
                        onCheckedChange = { isLocationEnabled = it }
                    )
                    SettingsClickableItem(
                        icon = painterResource(id = R.drawable.baseline_security),
                        title = "Seguridad",
                        description = "Configurar opciones de seguridad",
                        onClick = { navController.navigate(Routes.SETTINGS_SECURITY) }
                    )
                    SettingsClickableItem(
                        icon = painterResource(id = R.drawable.baseline_delete),
                        title = "Eliminar datos",
                        description = "Eliminar datos de la aplicación",
                        onClick = { showDeleteDialog = true }
                    )
                }

                SettingsCategory(title = "Sobre la aplicación") {
                    SettingsClickableItem(
                        icon = painterResource(id = R.drawable.baseline_info),
                        title = "Acerca de",
                        description = "Versión 1.0.0",
                        onClick = { showInfoDialog = true }
                    )
                    SettingsClickableItem(
                        icon = painterResource(id = R.drawable.baseline_help),
                        title = "Ayuda",
                        description = "Preguntas frecuentes y soporte",
                        onClick = { navController.navigate(Routes.SETTINGS_HELP) }
                    )
                    SettingsClickableItem(
                        icon = painterResource(id = R.drawable.baseline_policy),
                        title = "Términos y condiciones",
                        description = "Políticas de privacidad y uso",
                        onClick = { navController.navigate(Routes.SETTINGS_TERMS) }
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
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Cerrar sesión",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("Cerrar sesión")
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Reparalo v1.0.0",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    viewModel.deleteAppData(context)
                }) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            },
            title = { Text("Confirmar eliminación") },
            text = {
                Text("¿Estás seguro de que deseas eliminar todos los datos locales? Esta acción no se puede deshacer.")
            }
        )
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
            }
        )
    }

    if (showNotificationTypesDialog) {
        AlertDialog(
            onDismissRequest = { showNotificationTypesDialog = false },
            confirmButton = {
                TextButton(onClick = { showNotificationTypesDialog = false }) {
                    Text("Aceptar")
                }
            },
            title = { Text("Tipos de notificaciones") },
            text = {
                Text("Próximamente podrás configurar distintos tipos de notificaciones.")
            }
        )
    }
}
