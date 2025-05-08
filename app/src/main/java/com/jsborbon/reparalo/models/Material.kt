package com.jsborbon.reparalo.models

data class Material(
    val id: String,
    val name: String = "",
    val quantity: Int = 0,
    val description: String = "",
    val price: Float = 0f,
)
