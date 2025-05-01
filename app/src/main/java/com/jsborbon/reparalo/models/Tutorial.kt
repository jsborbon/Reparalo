package com.jsborbon.reparalo.models

import java.util.Date

data class Tutorial(
    val id: String,
    val titulo: String,
    val descripcion: String,
    val categoria: String,
    val nivelDificultad: String,
    val duracionEstimada: String,
    val materiales: List<Material>,
    val videoURL: String,
    val autor: String,
    val fechaPublicacion: Date,
    val calificacionPromedio: Double,
)
