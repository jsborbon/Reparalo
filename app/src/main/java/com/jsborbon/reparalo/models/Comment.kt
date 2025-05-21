package com.jsborbon.reparalo.models

import java.util.Date

data class Comment(
    val id: String = "",
    val tutorialId: String = "",
    val userId: String = "",
    val content: String = "",
    val date: Date = Date(),
    val rating: Int = 0,
)
