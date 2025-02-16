package com.jsborbon.relato.models
import java.util.UUID

data class Review(
    val id: UUID = UUID.randomUUID(),
    val reviewer: String,
    val title: String,
    val reviewBody: String,
    val starsGiven: Int
)
