package com.jsborbon.reparalo

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth

object Routes {
    const val SignUp = "SignUpView"
    const val Login = "LoginView"
    const val Home = "HomeView"
    const val Calendar = "CalendarView"
    const val Communications = "CommunicationsView"
    const val Configuration = "ConfigurationView"
    const val Management = "ManagementView"
    const val Insights = "InsightsView"
}

@Composable
fun NavigationWrapper (navHostController: NavHostController, auth: FirebaseAuth) {
    val currentUser = auth.currentUser
    val startDestination = if (currentUser != null) Routes.Home else Routes.Login

    NavHost(navController = navHostController, startDestination = startDestination) {

        composable(Routes.Login) {
            com.jsborbon.relato.LoginView(auth = auth, navController = navHostController) { user ->
                if (user != null) {
                    Log.d("Auth", "Login Successful: ${user.email}")
                    navHostController.navigate(Routes.Home) {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }
        }
        composable(Routes.SignUp) {
            com.jsborbon.relato.SignUpView(auth = auth, navController = navHostController) { user ->
                if (user != null) {
                    Log.d("Auth", "Registration Successful: ${user.email}")
                    navHostController.navigate(Routes.Home) {
                        popUpTo("signup") { inclusive = true }
                    }
                }
            }
        }
        composable(Routes.Home) { com.jsborbon.relato.HomeView(navHostController) }
        composable(Routes.Calendar) { com.jsborbon.relato.CalendarView(navHostController) }
        composable(Routes.Communications) { com.jsborbon.relato.CommunicationsView(navHostController) }
        composable(Routes.Configuration) { com.jsborbon.relato.ConfigurationView(navHostController) }
        composable(Routes.Management) { com.jsborbon.relato.ManagementView(navHostController) }
        composable(Routes.Insights) { com.jsborbon.relato.InsightsView(navHostController) }


    }
}

