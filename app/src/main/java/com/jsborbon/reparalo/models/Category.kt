package com.jsborbon.relato.models

import java.util.UUID

data class Category(
    val id: UUID = UUID.randomUUID(),
    val name: String
)