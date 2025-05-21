package com.jsborbon.reparalo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavigationWrapper(
    navController: NavHostController,
    auth: FirebaseAuth,
) {
    val currentUser = auth.currentUser
    val startDestination = if (currentUser != null) {
        Routes.DASHBOARD
    } else {
        Routes.SPLASH
    }

    AppNavGraph(
        navController = navController,
        startDestination = startDestination,
    )
}
