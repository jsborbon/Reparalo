package com.jsborbon.reparalo.models

import java.util.Date

data class Tutorial(
    var id: String = "",
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val difficultyLevel: String = "",
    val estimatedDuration: String = "",
    val materials: List<String> = emptyList(),
    val videoUrl: String = "",
    val author: Author = Author(),
    val publicationDate: Date = Date(),
    val averageRating: Float = 0f,
)
