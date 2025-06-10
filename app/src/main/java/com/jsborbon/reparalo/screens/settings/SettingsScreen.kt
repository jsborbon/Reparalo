package com.jsborbon.reparalo.screens.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jsborbon.reparalo.navigation.Routes
import com.jsborbon.reparalo.screens.settings.components.InfoDialog
import com.jsborbon.reparalo.screens.settings.components.NotificationTypesDialog
import com.jsborbon.reparalo.screens.settings.components.ResetSettingsDialog
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()
    val context = LocalContext.current

    val settingsManager = remember { SettingsManager.getInstance(context) }
    val settings by settingsManager.settings.collectAsState()

    var showInfoDialog by remember { mutableStateOf(false) }
    var showNotificationTypesDialog by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }

    val showScrollToTop by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 2 }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = { }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                state = listState,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    SettingSwitch(
                        title = "Tema oscuro",
                        description = "Activar o desactivar modo oscuro",
                        checked = settings.isDarkTheme,
                        onCheckedChange = {
                            settingsManager.updateDarkTheme(it)
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = if (it) "Tema oscuro activado" else "Tema claro activado",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    )
                }
                item {
                    SettingSwitch(
                        title = "Notificaciones",
                        description = "Recibir notificaciones de la app",
                        checked = settings.isNotificationsEnabled,
                        onCheckedChange = {
                            settingsManager.updateNotifications(it)
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = if (it) "Notificaciones activadas" else "Notificaciones desactivadas",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    )
                }
                item {
                    SettingSwitch(
                        title = "Ubicación",
                        description = "Permitir uso de ubicación",
                        checked = settings.isLocationEnabled,
                        onCheckedChange = {
                            settingsManager.updateLocation(it, context)
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = if (it) "Ubicación activada" else "Ubicación desactivada",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    )
                }
                item {
                    SettingSwitch(
                        title = "Autenticación biométrica",
                        description = "Usar huella o reconocimiento facial",
                        checked = settings.isBiometricEnabled,
                        onCheckedChange = {
                            settingsManager.updateBiometric(it, context)
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = if (it) "Autenticación biométrica activada" else "Autenticación biométrica desactivada",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    )
                }
                item {
                    SettingSwitch(
                        title = "Sincronización automática",
                        description = "Sincronizar datos en segundo plano",
                        checked = settings.isAutoSyncEnabled,
                        onCheckedChange = {
                            settingsManager.updateAutoSync(it)
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = if (it) "Sincronización automática activada" else "Sincronización automática desactivada",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    )
                }
                item {
                    HorizontalDivider()
                }
                item {
                    SettingButton(
                        title = "Tipos de notificación",
                        description = settings.selectedNotificationTypes.joinToString(", "),
                        onClick = { showNotificationTypesDialog = true }
                    )
                }
                item {
                    SettingButton(
                        title = "Restablecer configuración",
                        description = "Restaurar valores predeterminados",
                        onClick = { showResetDialog = true }
                    )
                }
                item {
                    SettingButton(
                        title = "Términos y condiciones",
                        description = "Ver términos de uso y políticas",
                        onClick = { navController.navigate(Routes.SETTINGS_TERMS) }
                    )
                }
                item {
                    SettingButton(
                        title = "Información de la aplicación",
                        description = "Versión, créditos y más",
                        onClick = { showInfoDialog = true }
                    )
                }
            }

            AnimatedVisibility(
                visible = showScrollToTop,
                enter = slideInVertically(initialOffsetY = { it }, animationSpec = spring()) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }, animationSpec = spring()) + fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .semantics { contentDescription = "Botón para ir arriba" }
            ) {
                IconButton(
                    onClick = {
                        scope.launch {
                            listState.animateScrollToItem(0)
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowUp,
                        contentDescription = "Ir arriba",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }

    // Diálogos
    if (showInfoDialog) {
        InfoDialog(onDismiss = { showInfoDialog = false })
    }

    if (showNotificationTypesDialog) {
        NotificationTypesDialog(
            selectedTypes = settings.selectedNotificationTypes,
            onToggleType = { type ->
                settingsManager.toggleNotificationType(type)
            },
            onDismiss = { showNotificationTypesDialog = false }
        )
    }

    if (showResetDialog) {
        ResetSettingsDialog(
            onConfirm = {
                settingsManager.resetSettings()
                showResetDialog = false
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Configuración restablecida",
                        duration = SnackbarDuration.Short
                    )
                }
            },
            onDismiss = { showResetDialog = false }
        )
    }
}

@Composable
private fun SettingSwitch(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        tonalElevation = 1.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ) {
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = checked,
                    onCheckedChange = onCheckedChange,
                    modifier = Modifier.semantics {
                        contentDescription = "$title switch"
                    }
                )
            }
        }
    }
}

@Composable
private fun SettingButton(
    title: String,
    description: String,
    onClick: () -> Unit,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        tonalElevation = 1.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        TextButton(
            onClick = onClick,
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
                .semantics {
                    contentDescription = "$title button"
                }
        ) {
            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = titleColor
                    )
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
