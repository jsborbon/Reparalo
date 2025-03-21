package com.jsborbon.reparalo.models

import java.util.UUID

data class Category(
    val id: UUID = UUID.randomUUID(),
    val name: String
)