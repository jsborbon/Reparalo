package com.jsborbon.reparalo.models

data class Settings(
    val isDarkTheme: Boolean = false,
    val isNotificationsEnabled: Boolean = true,
    val isLocationEnabled: Boolean = false,
    val isBiometricEnabled: Boolean = false,
    val isAutoSyncEnabled: Boolean = true,
    val selectedNotificationTypes: Set<String> = setOf("Promociones", "Novedades")
)
