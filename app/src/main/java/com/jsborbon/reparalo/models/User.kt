package com.jsborbon.reparalo.models

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val userType: UserType = UserType.CLIENT,
    val availability: String = "",
    val registrationDate: String = "",
    val specialty: String? = null,
    val rating: Float = 0f,
    val satisfaction: Float = 0f,
    val completedServices: Int = 0,
    val favoriteCount: Int = 0,
    val lastServiceTimestamp: Long = 0L,
)
