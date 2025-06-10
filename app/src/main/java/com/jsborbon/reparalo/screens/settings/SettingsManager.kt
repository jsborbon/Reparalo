package com.jsborbon.reparalo.screens.settings

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.biometric.BiometricManager
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.jsborbon.reparalo.models.Settings
import com.jsborbon.reparalo.screens.settings.ThemeManager

class SettingsManager private constructor(context: Context) {

    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)

    private val _settings = MutableStateFlow(loadSettingsFromPrefs())
    val settings: StateFlow<Settings> = _settings.asStateFlow()

    companion object {
        @Volatile
        private var INSTANCE: SettingsManager? = null

        fun getInstance(context: Context): SettingsManager =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: SettingsManager(context.applicationContext).also { INSTANCE = it }
            }
    }

    private fun loadSettingsFromPrefs(): Settings = Settings(
        isDarkTheme = sharedPrefs.getBoolean("dark_theme", false),
        isNotificationsEnabled = sharedPrefs.getBoolean("notifications", true),
        isLocationEnabled = sharedPrefs.getBoolean("location", false),
        isBiometricEnabled = sharedPrefs.getBoolean("biometric", false),
        isAutoSyncEnabled = sharedPrefs.getBoolean("auto_sync", true),
        selectedNotificationTypes = sharedPrefs.getStringSet(
            "notification_types",
            setOf("Promociones", "Novedades")
        )?.toSet() ?: setOf("Promociones", "Novedades")
    )

    fun updateDarkTheme(enabled: Boolean) {
        sharedPrefs.edit { putBoolean("dark_theme", enabled) }
        _settings.value = _settings.value.copy(isDarkTheme = enabled)
        // Apply theme immediately
        ThemeManager.setDarkTheme(enabled)
    }

    fun updateNotifications(enabled: Boolean) {
        sharedPrefs.edit { putBoolean("notifications", enabled) }
        _settings.value = _settings.value.copy(isNotificationsEnabled = enabled)
    }

    fun updateLocation(enabled: Boolean, context: Context) {
        if (enabled) {
            val hasLocationPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasLocationPermission) {
                // Permission missing, do not update
                return
            }
        }

        sharedPrefs.edit { putBoolean("location", enabled) }
        _settings.value = _settings.value.copy(isLocationEnabled = enabled)
    }

    fun updateBiometric(enabled: Boolean, context: Context) {
        if (enabled) {
            val biometricManager = BiometricManager.from(context)
            val canAuthenticate = biometricManager.canAuthenticate(
                BiometricManager.Authenticators.BIOMETRIC_WEAK
            )
            if (canAuthenticate != BiometricManager.BIOMETRIC_SUCCESS) {
                // Device can't authenticate biometrically, do not update
                return
            }
        }

        sharedPrefs.edit { putBoolean("biometric", enabled) }
        _settings.value = _settings.value.copy(isBiometricEnabled = enabled)
    }

    fun updateAutoSync(enabled: Boolean) {
        sharedPrefs.edit { putBoolean("auto_sync", enabled) }
        _settings.value = _settings.value.copy(isAutoSyncEnabled = enabled)
    }

    fun toggleNotificationType(type: String) {
        val currentTypes = _settings.value.selectedNotificationTypes
        val newTypes = if (currentTypes.contains(type)) {
            currentTypes - type
        } else {
            currentTypes + type
        }

        sharedPrefs.edit { putStringSet("notification_types", newTypes) }
        _settings.value = _settings.value.copy(selectedNotificationTypes = newTypes)
    }

    fun resetSettings() {
        val defaults = Settings()

        sharedPrefs.edit {
            putBoolean("dark_theme", defaults.isDarkTheme)
            putBoolean("notifications", defaults.isNotificationsEnabled)
            putBoolean("location", defaults.isLocationEnabled)
            putBoolean("biometric", defaults.isBiometricEnabled)
            putBoolean("auto_sync", defaults.isAutoSyncEnabled)
            putStringSet("notification_types", defaults.selectedNotificationTypes)
        }

        _settings.value = defaults
        ThemeManager.setDarkTheme(defaults.isDarkTheme)
    }
}
