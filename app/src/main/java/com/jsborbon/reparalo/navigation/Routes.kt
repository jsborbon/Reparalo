package com.jsborbon.reparalo.navigation

object Routes {
    const val SPLASH = "splash"
    const val AUTHENTICATION = "authentication"
    const val DASHBOARD = "dashboard"

    const val USER_PROFILE = "user_profile"
    const val SETTINGS = "settings"
    const val SETTINGS_HELP = "settings_help"
    const val SETTINGS_SECURITY = "settings_security"
    const val SETTINGS_TERMS = "settings_terms"
    const val SETTINGS_PASSWORD = "settings_password"
    const val NOTIFICATIONS = "notifications"
    const val FAVORITES = "favorites"
    const val SERVICE_HISTORY = "service_history"

    const val TECHNICIAN_SEARCH = "technician_search"
    const val TECHNICIAN_PROFILE = "technician_profile"
    const val PROFESSIONAL_CONNECTION = "professional_connection"

    const val TUTORIALS = "tutorials"
    const val TUTORIAL_DETAIL = "tutorial_detail"
    const val TUTORIAL_CREATE = "tutorial_create"
    const val TUTORIAL_EDIT = "tutorial_edit"

    const val MATERIALS_LIST = "materials_list"
    const val MATERIAL_DETAIL = "material_detail"
    const val MATERIAL_EDIT = "material_edit"

    const val FORUM = "forum"
    const val FORUM_CREATE = "forum_create"
    const val FORUM_SEARCH = "forum_search"
    const val FORUM_TOPIC_DETAIL = "forum_topic_detail"
    const val FORUM_EDIT = "forum_edit"

    fun forumTopicDetailWithId(topicId: String): String = "$FORUM_TOPIC_DETAIL/$topicId"
    fun forumEditWithId(topicId: String): String = "$FORUM_EDIT/$topicId"
    fun tutorialDetailWithId(tutorialId: String): String = "$TUTORIAL_DETAIL/$tutorialId"
    fun tutorialEditWithId(tutorialId: String): String = "$TUTORIAL_EDIT/$tutorialId"
    fun materialDetailWithId(materialId: String): String = "$MATERIAL_DETAIL/$materialId" //TODO
    fun materialEditWithId(materialId: String): String = "$MATERIAL_EDIT/$materialId" //TODO
    fun technicianProfileWithId(technicianId: String): String = "$TECHNICIAN_PROFILE/$technicianId"
}
