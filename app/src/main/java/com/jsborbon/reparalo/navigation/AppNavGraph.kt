package com.jsborbon.reparalo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jsborbon.reparalo.screens.MainScreen
import com.jsborbon.reparalo.screens.SplashScreen
import com.jsborbon.reparalo.screens.authentication.AuthenticationScreen
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
import com.jsborbon.reparalo.screens.profile.UserProfileScreen
import com.jsborbon.reparalo.screens.settings.SettingsHelpScreen
import com.jsborbon.reparalo.screens.settings.SettingsScreen
import com.jsborbon.reparalo.screens.settings.SettingsTermsScreen
import com.jsborbon.reparalo.screens.technician.ProfessionalConnectionScreen
import com.jsborbon.reparalo.screens.technician.TechnicianProfileScreen
import com.jsborbon.reparalo.screens.technician.TechnicianSearchScreen
import com.jsborbon.reparalo.screens.tutorial.TutorialCreateScreen
import com.jsborbon.reparalo.screens.tutorial.TutorialDetailScreen
import com.jsborbon.reparalo.screens.tutorial.TutorialEditScreen

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

        composable(Routes.DASHBOARD) {
            MainScreen(navController = navController)
        }

        composable(Routes.TECHNICIAN_SEARCH) {
            TechnicianSearchScreen(navController = navController)
        }

        composable("${Routes.TUTORIAL_DETAIL}/{tutorialId}") { backStackEntry ->
            val tutorialId = backStackEntry.arguments?.getString("tutorialId").orEmpty()
            TutorialDetailScreen(navController = navController, tutorialId = tutorialId)
        }

        composable("${Routes.TECHNICIAN_PROFILE}/{technicianId}") { backStackEntry ->
            val technicianId = backStackEntry.arguments?.getString("technicianId").orEmpty()
            TechnicianProfileScreen(navController = navController, technicianId = technicianId)
        }

        composable(Routes.FORUM) {
            ForumScreen(navController = navController)
        }

        composable(Routes.FORUM_CREATE) {
            ForumCreateScreen(navController = navController)
        }

        composable(Routes.FORUM_SEARCH) {
            ForumSearchScreen(navController = navController)
        }

        composable("${Routes.FORUM_EDIT}/{topicId}") { backStackEntry ->
            val topicId = backStackEntry.arguments?.getString("topicId") ?: return@composable
            ForumEditScreen(navController = navController, topicId = topicId)
        }

        composable(Routes.SERVICE_HISTORY) {
            ServiceHistoryScreen(navController = navController)
        }

        composable(
            route = "${Routes.USER_PROFILE}?edit={edit}",
            arguments = listOf(navArgument("edit") { defaultValue = false }),
        ) { backStackEntry ->
            val isEdit = backStackEntry.arguments?.getBoolean("edit") ?: false
            UserProfileScreen(navController = navController, isEditMode = isEdit)
        }

        composable("${Routes.FORUM_TOPIC_DETAIL}/{topicId}") { backStackEntry ->
            val topicId = backStackEntry.arguments?.getString("topicId").orEmpty()
            ForumTopicDetailScreen(navController = navController, topicId = topicId)
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
            FavoritesScreen(navController = navController)
        }

        composable(Routes.MATERIALS_LIST) {
            MaterialsListScreen(navController = navController)
        }

        composable(Routes.PROFESSIONAL_CONNECTION) {
            ProfessionalConnectionScreen(navController = navController)
        }

        composable(Routes.NOTIFICATIONS) {
            NotificationScreen(navController = navController)
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(navController = navController)
        }

        composable(Routes.SETTINGS_HELP) {
            SettingsHelpScreen(navController = navController)
        }

        composable(Routes.SETTINGS_TERMS) {
            SettingsTermsScreen(navController = navController)
        }

        composable(Routes.SETTINGS_PASSWORD) {
            ChangePasswordScreen(navController = navController)
        }

        composable("${Routes.MATERIAL_DETAIL}/{materialId}") { backStackEntry ->
            val materialId = backStackEntry.arguments?.getString("materialId").orEmpty()
            MaterialDetailScreen(navController = navController, materialId = materialId)
        }

        composable("${Routes.MATERIAL_EDIT}/{materialId}") { backStackEntry ->
            val materialId = backStackEntry.arguments?.getString("materialId").orEmpty()
            MaterialEditScreen(navController = navController, materialId = materialId)
        }
    }
}
