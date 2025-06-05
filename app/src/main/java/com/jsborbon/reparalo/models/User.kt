package com.jsborbon.reparalo.models

import java.util.Date

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val userType: UserType = UserType.CLIENT,
    val availability: String = "",
    val registrationDate: Date = Date(),
    val specialty: String? = null,
    val rating: Float = 0f,
    val satisfaction: Float = 0f,
    val completedServices: Int = 0,
    val favoriteCount: Int = 0,
    val lastServiceTimestamp: Long = 0L,
    val favorites: List<String> = emptyList(),
)
