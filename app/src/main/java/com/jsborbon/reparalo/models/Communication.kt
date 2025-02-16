package com.jsborbon.relato.models

import java.util.UUID

data class Communication(
    val id: UUID = UUID.randomUUID(),
    val subject: String,
    val communicationBody: String,
    val sendingGroup: String,
    val campaign: String,
    val future: Boolean
)
