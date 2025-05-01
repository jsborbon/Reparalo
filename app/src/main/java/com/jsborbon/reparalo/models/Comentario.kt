package com.jsborbon.reparalo.models

import java.util.Date

data class Comentario(
    val id: String,
    val idTutorial: String,
    val uidUsuario: String,
    val contenido: String,
    val fecha: Date,
    val calificacion: Int,
)
