package com.jsborbon.reparalo

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.jsborbon.reparalo.navigation.NavigationWrapper
import com.jsborbon.reparalo.ui.theme.ReparaloTheme

/**
 * Entry point of the Reparalo application.
 * Sets up window behavior, visual theming, and main navigation.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // Prevent screenshots in sensitive areas (security best practice)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        setContent {
            ReparaloTheme {
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
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

        // Listen for Firebase Auth state changes if needed
        LaunchedEffect(firebaseAuth) {
            // FirebaseAuth.addAuthStateListener can be placed here
        }

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

            // Enable drawing behind system bars
            WindowCompat.setDecorFitsSystemWindows(window, false)

            // Adjust bar icon appearance based on luminance
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !isDarkTheme
            insetsController.isAppearanceLightNavigationBars = !isDarkTheme
        }
    }
}
