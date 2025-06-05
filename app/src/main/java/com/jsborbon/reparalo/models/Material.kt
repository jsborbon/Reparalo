package com.jsborbon.reparalo.models

import java.util.UUID

data class Material(
    var id: String = UUID.randomUUID().toString(),
    var name: String = "",
    var quantity: Int = 0,
    var description: String = "",
    var price: Float = 0f,
)
