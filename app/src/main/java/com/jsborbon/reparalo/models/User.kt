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
)
