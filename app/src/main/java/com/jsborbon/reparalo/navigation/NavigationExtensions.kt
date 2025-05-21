package com.jsborbon.reparalo.navigation

import androidx.navigation.NavController

//TODO: To check this file, it seems to be a copy of the NavigationWrapper.kt file

fun NavController.navigateToSplash() {
    if (currentDestination?.route != Routes.SPLASH) {
        navigate(Routes.SPLASH)
    }
}

fun NavController.navigateToAuthentication() {
    if (currentDestination?.route != Routes.AUTHENTICATION) {
        navigate(Routes.AUTHENTICATION)
    }
}

fun NavController.navigateToDashboard() {
    if (currentDestination?.route != Routes.DASHBOARD) {
        navigate(Routes.DASHBOARD)
    }
}

fun NavController.navigateToTutorials() {
    if (currentDestination?.route != Routes.TUTORIALS) {
        navigate(Routes.TUTORIALS)
    }
}

fun NavController.navigateToTutorialDetail(tutorialId: String) {
    val route = Routes.tutorialDetailWithId(tutorialId)
    if (currentDestination?.route != route) {
        navigate(route)
    }
}

fun NavController.navigateToTutorialEdit(tutorialId: String) {
    val route = Routes.tutorialEditWithId(tutorialId)
    if (currentDestination?.route != route) {
        navigate(route)
    }
}

fun NavController.navigateToTutorialCreate() {
    if (currentDestination?.route != Routes.TUTORIAL_CREATE) {
        navigate(Routes.TUTORIAL_CREATE)
    }
}

fun NavController.navigateToMaterialsList() {
    if (currentDestination?.route != Routes.MATERIALS_LIST) {
        navigate(Routes.MATERIALS_LIST)
    }
}

fun NavController.navigateToProfessionalConnection() {
    if (currentDestination?.route != Routes.PROFESSIONAL_CONNECTION) {
        navigate(Routes.PROFESSIONAL_CONNECTION)
    }
}

fun NavController.navigateToForum() {
    safeNavigate(Routes.FORUM)
}

fun NavController.navigateToForumSearch() {
    if (currentDestination?.route != Routes.FORUM_SEARCH) {
        navigate(Routes.FORUM_SEARCH)
    }
}

fun NavController.navigateToForumCreate() {
    if (currentDestination?.route != Routes.FORUM_CREATE) {
        navigate(Routes.FORUM_CREATE)
    }
}

fun NavController.navigateToForumTopicDetail(topicId: String) {
    val route = Routes.forumTopicDetailWithId(topicId)
    if (currentDestination?.route != route) {
        navigate(route)
    }
}

fun NavController.navigateToForumEdit(topicId: String) {
    val route = Routes.forumEditWithId(topicId)
    if (currentDestination?.route != route) {
        navigate(route)
    }
}

fun NavController.navigateToTechnicianSearch() {
    if (currentDestination?.route != Routes.TECHNICIAN_SEARCH) {
        navigate(Routes.TECHNICIAN_SEARCH)
    }
}

fun NavController.navigateToTechnicianProfile(technicianId: String) {
    val route = Routes.technicianProfileWithId(technicianId)
    if (currentDestination?.route != route) {
        navigate(route)
    }
}

fun NavController.navigateToUserProfile() {
    if (currentDestination?.route != Routes.USER_PROFILE) {
        navigate(Routes.USER_PROFILE)
    }
}

fun NavController.navigateToNotifications() {
    if (currentDestination?.route != Routes.NOTIFICATIONS) {
        navigate(Routes.NOTIFICATIONS)
    }
}

fun NavController.navigateToSettings() {
    if (currentDestination?.route != Routes.SETTINGS) {
        navigate(Routes.SETTINGS)
    }
}

fun NavController.navigateToSettingsHelp() {
    if (currentDestination?.route != Routes.SETTINGS_HELP) {
        navigate(Routes.SETTINGS_HELP)
    }
}

fun NavController.navigateToSettingsSecurity() {
    if (currentDestination?.route != Routes.SETTINGS_SECURITY) {
        navigate(Routes.SETTINGS_SECURITY)
    }
}

fun NavController.navigateToSettingsTerms() {
    if (currentDestination?.route != Routes.SETTINGS_TERMS) {
        navigate(Routes.SETTINGS_TERMS)
    }
}

fun NavController.navigateToServiceHistory() {
    if (currentDestination?.route != Routes.SERVICE_HISTORY) {
        navigate(Routes.SERVICE_HISTORY)
    }
}

fun NavController.navigateToSettingsPassword() {
    if (currentDestination?.route != Routes.SETTINGS_PASSWORD) {
        navigate(Routes.SETTINGS_PASSWORD)
    }
}

fun NavController.navigateAndPopUpTo(
    route: String,
    popUpToRoute: String,
    inclusive: Boolean = false,
) {
    navigate(route) {
        popUpTo(popUpToRoute) { this.inclusive = inclusive }
        launchSingleTop = true
    }
}

fun NavController.safeNavigate(route: String) {
    if (currentDestination?.route != route) {
        navigate(route)
    }
}

fun NavController.goBack() {
    popBackStack()
}
