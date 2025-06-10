package com.jsborbon.reparalo.screens.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jsborbon.reparalo.R
import com.jsborbon.reparalo.navigation.Routes
import com.jsborbon.reparalo.screens.settings.components.InfoDialog
import com.jsborbon.reparalo.screens.settings.components.LogoutConfirmationDialog
import com.jsborbon.reparalo.screens.settings.components.NotificationTypesDialog
import com.jsborbon.reparalo.screens.settings.components.ResetSettingsDialog
import com.jsborbon.reparalo.viewmodels.AuthViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val authViewModel: AuthViewModel = viewModel()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()

    var isDarkTheme by remember { mutableStateOf(false) }
    var isNotificationsEnabled by remember { mutableStateOf(true) }
    var isLocationEnabled by remember { mutableStateOf(false) }
    var isBiometricEnabled by remember { mutableStateOf(false) }
    var isAutoSyncEnabled by remember { mutableStateOf(true) }

    var showLogoutDialog by remember { mutableStateOf(false) }
    var showInfoDialog by remember { mutableStateOf(false) }
    var showNotificationTypesDialog by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }

    var selectedNotificationTypes by remember {
        mutableStateOf(setOf("Promociones", "Novedades"))
    }

    val showScrollToTop by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 2 }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Configuración",
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver atrás"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                state = listState,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // ... contenido omitido por brevedad
            }

            AnimatedVisibility(
                visible = showScrollToTop,
                enter = slideInVertically(initialOffsetY = { it }, animationSpec = spring()) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }, animationSpec = spring()) + fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
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
                            MaterialTheme.colorScheme.primaryContainer,
                            CircleShape
                        )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_expand_circle_up),
                        contentDescription = "Ir arriba",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }

    if (showLogoutDialog) {
        LogoutConfirmationDialog(
            onConfirm = {
                authViewModel.logout()
                navController.navigate(Routes.AUTHENTICATION) {
                    popUpTo(0) { inclusive = true }
                }
                showLogoutDialog = false
            },
            onDismiss = { showLogoutDialog = false }
        )
    }

    if (showInfoDialog) {
        InfoDialog(onDismiss = { showInfoDialog = false })
    }

    if (showNotificationTypesDialog) {
        NotificationTypesDialog(
            selectedTypes = selectedNotificationTypes,
            onToggleType = { type ->
                selectedNotificationTypes = if (selectedNotificationTypes.contains(type)) {
                    selectedNotificationTypes - type
                } else {
                    selectedNotificationTypes + type
                }
            },
            onDismiss = { showNotificationTypesDialog = false }
        )
    }

    if (showResetDialog) {
        ResetSettingsDialog(
            onConfirm = {
                isDarkTheme = false
                isNotificationsEnabled = true
                isLocationEnabled = false
                isBiometricEnabled = false
                isAutoSyncEnabled = true
                selectedNotificationTypes = setOf("Promociones", "Novedades")
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
