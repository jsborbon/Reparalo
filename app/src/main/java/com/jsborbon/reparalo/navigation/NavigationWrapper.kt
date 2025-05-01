package com.jsborbon.reparalo.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import com.jsborbon.reparalo.screens.AuthenticationScreen
import com.jsborbon.reparalo.screens.MainScreen
import com.jsborbon.reparalo.screens.SplashScreen
import com.jsborbon.reparalo.screens.TutorialDetailScreen

object Routes {
    const val SPLASH = "SplashScreen"
    const val AUTHENTICATION = "AuthenticationScreen"
    const val DASHBOARD = "DashboardScreen"
    const val TUTORIALS = "TutorialsScreen"
    const val TUTORIAL_DETAIL = "TutorialDetailScreen"
    const val MATERIALS_LIST = "MaterialsListScreen"
    const val PROFESSIONAL_CONNECTION = "ProfessionalConnectionScreen"
    const val FORUM = "ForumScreen"
    const val USER_PROFILE = "ProfileScreen"
    const val NOTIFICATIONS = "NotificationsScreen"
    const val SETTINGS = "SettingsScreen"
}

@Composable
fun NavigationWrapper(
    navHostController: NavHostController,
    auth: FirebaseAuth,
) {
    val currentUser = auth.currentUser
    val startDestination = if (currentUser != null) Routes.DASHBOARD else Routes.SPLASH

    NavHost(
        navController = navHostController,
        startDestination = startDestination,
    ) {
        composable(Routes.SPLASH) { SplashScreen(navHostController) }

        composable(Routes.AUTHENTICATION) { AuthenticationScreen(navHostController) }

        composable(Routes.DASHBOARD) { MainScreen() }

        composable(Routes.TUTORIALS) {
            // Implementación pendiente
            Text("Tutoriales - Próximamente", modifier = Modifier.padding(16.dp))
        }

        composable("${Routes.TUTORIAL_DETAIL}/{id}") { backStackEntry ->
            val tutorialId = backStackEntry.arguments?.getString("id") ?: ""
            TutorialDetailScreen(navHostController, tutorialId)
        }

        composable(Routes.MATERIALS_LIST) {
            // Implementación pendiente
            Text("Lista de Materiales - Próximamente", modifier = Modifier.padding(16.dp))
        }

        composable(Routes.PROFESSIONAL_CONNECTION) {
            // Implementación pendiente
            Text("Conexión con Profesionales - Próximamente", modifier = Modifier.padding(16.dp))
        }

        composable(Routes.FORUM) {
            // Implementación pendiente
            Text("Foro - Próximamente", modifier = Modifier.padding(16.dp))
        }

        composable(Routes.USER_PROFILE) {
            // Implementación pendiente
            Text("Perfil de Usuario - Próximamente", modifier = Modifier.padding(16.dp))
        }

        composable(Routes.NOTIFICATIONS) {
            // Implementación pendiente
            Text("Notificaciones - Próximamente", modifier = Modifier.padding(16.dp))
        }

        composable(Routes.SETTINGS) {
            // Implementación pendiente
            Text("Configuración - Próximamente", modifier = Modifier.padding(16.dp))
        }
    }
}
