package com.jsborbon.reparalo.models

import java.util.Date

data class Tutorial(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val difficultyLevel: String,
    val estimatedDuration: String,
    val materials: List<Material>,
    val videoUrl: String,
    val author: String,
    val publicationDate: Date,
    val averageRating: Double,
)
