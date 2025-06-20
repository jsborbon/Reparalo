package com.jsborbon.reparalo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jsborbon.reparalo.screens.authentication.AuthenticationScreen
import com.jsborbon.reparalo.screens.authentication.ForgotPasswordScreen
import com.jsborbon.reparalo.screens.dashboard.DashboardScreen
import com.jsborbon.reparalo.screens.forum.ForumCreateScreen
import com.jsborbon.reparalo.screens.forum.ForumEditScreen
import com.jsborbon.reparalo.screens.forum.ForumScreen
import com.jsborbon.reparalo.screens.forum.ForumSearchScreen
import com.jsborbon.reparalo.screens.forum.ForumTopicDetailScreen
import com.jsborbon.reparalo.screens.history.ServiceHistoryScreen
import com.jsborbon.reparalo.screens.material.MaterialDetailScreen
import com.jsborbon.reparalo.screens.material.MaterialEditScreen
import com.jsborbon.reparalo.screens.material.MaterialsListScreen
import com.jsborbon.reparalo.screens.notifications.NotificationScreen
import com.jsborbon.reparalo.screens.profile.ChangePasswordScreen
import com.jsborbon.reparalo.screens.profile.FavoritesScreen
import com.jsborbon.reparalo.screens.settings.SettingsScreen
import com.jsborbon.reparalo.screens.settings.terms.SettingsTermsScreen
import com.jsborbon.reparalo.screens.splashScreen.SplashScreen
import com.jsborbon.reparalo.screens.technician.ProfessionalConnectionScreen
import com.jsborbon.reparalo.screens.technician.profile.TechnicianProfileScreen
import com.jsborbon.reparalo.screens.profile.UserProfileScreen
import com.jsborbon.reparalo.screens.technician.searching.TechnicianSearchScreen
import com.jsborbon.reparalo.screens.tutorial.TutorialCreateScreen
import com.jsborbon.reparalo.screens.tutorial.TutorialDetailScreen
import com.jsborbon.reparalo.screens.tutorial.TutorialEditScreen
import com.jsborbon.reparalo.screens.tutorial.TutorialsScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(Routes.SPLASH) {
            SplashScreen(navController = navController)
        }

        composable(Routes.AUTHENTICATION) {
            AuthenticationScreen(navController = navController)
        }

        composable(Routes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(
                onBack = { navController.popBackStack() },
            )
        }

        composable(Routes.DASHBOARD) {
            DashboardScreen(navController = navController)
        }

        composable(Routes.TUTORIALS) {
            TutorialsScreen(navController = navController)
        }

        composable(Routes.MATERIALS_LIST) {
            MaterialsListScreen(navController = navController)
        }

        composable(Routes.FORUM) {
            ForumScreen(navController = navController)
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(navController = navController)
        }


        composable(Routes.TECHNICIAN_SEARCH) {
            TechnicianSearchScreen(navController = navController)
        }

        composable("${Routes.TUTORIAL_DETAIL}/{tutorialId}") { backStackEntry ->
            val tutorialId = backStackEntry.arguments?.getString("tutorialId").orEmpty()
            TutorialDetailScreen(
                navController = navController,
                tutorialId = tutorialId,
            )
        }

        composable("${Routes.TECHNICIAN_PROFILE}/{technicianId}") { backStackEntry ->
            val technicianId = backStackEntry.arguments?.getString("technicianId").orEmpty()
            TechnicianProfileScreen(navController = navController, technicianId = technicianId)
        }

        composable(Routes.FORUM_CREATE) {
            ForumCreateScreen(navController = navController)
        }

        composable(Routes.FORUM_SEARCH) {
            ForumSearchScreen(navController = navController)
        }

        composable("${Routes.FORUM_EDIT}/{topicId}") { backStackEntry ->
            val topicId = backStackEntry.arguments?.getString("topicId").orEmpty()
            ForumEditScreen(navController = navController, topicId = topicId)
        }

        composable(Routes.SERVICE_HISTORY) {
            ServiceHistoryScreen(navController = navController)
        }

        composable(Routes.USER_PROFILE) {
            UserProfileScreen(navController = navController)
        }

        composable(Routes.NOTIFICATIONS) {
            NotificationScreen(navController)
        }

        composable(Routes.CHANGE_PASSWORD) {
            ChangePasswordScreen(navController)
        }

        composable(Routes.TUTORIAL_CREATE) {
            TutorialCreateScreen(navController = navController)
        }

        composable(
            route = "${Routes.TUTORIAL_EDIT}/{tutorialId}",
            arguments = listOf(navArgument("tutorialId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val tutorialId = backStackEntry.arguments?.getString("tutorialId").orEmpty()
            TutorialEditScreen(navController = navController, tutorialId = tutorialId)
        }

        composable(Routes.FAVORITES) {
            FavoritesScreen(
                navController = navController,
            )
        }

        composable("${Routes.MATERIAL_DETAIL}/{materialId}") { backStackEntry ->
            val materialId = backStackEntry.arguments?.getString("materialId").orEmpty()
            MaterialDetailScreen(navController = navController, materialId = materialId)
        }

        composable("${Routes.MATERIAL_EDIT}/{materialId}") { backStackEntry ->
            val materialId = backStackEntry.arguments?.getString("materialId").orEmpty()
            MaterialEditScreen(navController = navController, materialId = materialId)
        }

        composable(Routes.PROFESSIONAL_CONNECTION) {
            ProfessionalConnectionScreen(navController = navController)
        }

        composable(Routes.SETTINGS_TERMS) {
            SettingsTermsScreen(navController = navController)
        }

        composable(
            route = "${Routes.FORUM_TOPIC_DETAIL}/{topicId}",
            arguments = listOf(navArgument("topicId") { type = NavType.StringType })
        ) { backStackEntry ->
            val topicId = backStackEntry.arguments?.getString("topicId").orEmpty()
            ForumTopicDetailScreen(navController = navController, topicId = topicId)
        }

        composable("${Routes.FORUM_TOPIC_DETAIL}/{topicId}") { backStackEntry ->
            val topicId = backStackEntry.arguments?.getString("topicId").orEmpty()
            ForumTopicDetailScreen(navController = navController, topicId = topicId)
        }

    }
}
