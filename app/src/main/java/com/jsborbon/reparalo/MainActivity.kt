package com.jsborbon.reparalo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.jsborbon.reparalo.navigation.NavigationWrapper
import com.jsborbon.reparalo.screens.settings.SettingsManager
import com.jsborbon.reparalo.screens.settings.ThemeManager
import com.jsborbon.reparalo.ui.theme.ReparaloTheme

/**
 * Entry point of the Reparalo application.
 * Sets up window behavior, visual theming, and main navigation.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val settingsManager = SettingsManager.getInstance(this)

        setContent {
            // Collect user settings state
            val settings by settingsManager.settings.collectAsState()

            // Synchronize ThemeManager with saved settings on first composition
            LaunchedEffect(settings.isDarkTheme) {
                ThemeManager.setDarkTheme(settings.isDarkTheme)
            }

            ReparaloTheme(
                darkTheme = settings.isDarkTheme,
                dynamicColor = false // Set to true if you want dynamic colors
            ) {
                ConfigureSystemBars()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainActivityContent()
                }
            }
        }
    }

    /**
     * Composable content that initializes navigation and handles auth logic.
     */
    @Composable
    private fun MainActivityContent() {
        val navController: NavHostController = rememberNavController()
        val firebaseAuth = FirebaseAuth.getInstance()

        NavigationWrapper(
            navController = navController,
            auth = firebaseAuth
        )
    }

    /**
     * Configures system UI bars (status & navigation) to align with app theme.
     */
    @Composable
    private fun ConfigureSystemBars() {
        val view = LocalView.current
        val activity = view.context as ComponentActivity
        val isDarkTheme = MaterialTheme.colorScheme.surface.luminance() <= 0.5f

        SideEffect {
            val window = activity.window

            WindowCompat.setDecorFitsSystemWindows(window, false)

            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !isDarkTheme
            insetsController.isAppearanceLightNavigationBars = !isDarkTheme
        }
    }
}
