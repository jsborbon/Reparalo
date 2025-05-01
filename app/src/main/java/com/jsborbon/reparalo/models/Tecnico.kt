package com.jsborbon.reparalo.models

data class Tecnico(
    val usuario: Usuario,
    val especialidades: List<String>,
    val calificacionPromedio: Double,
    val numeroServicios: Int,
    val verificado: Boolean,
    val ubicacion: String? = null,
    val disponibilidad: String,
)
