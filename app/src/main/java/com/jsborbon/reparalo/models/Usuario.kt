package com.jsborbon.reparalo.models

import java.util.Date

data class Usuario(
    val uid: String,
    val nombre: String,
    val email: String,
    val telefono: String,
    val tipoUsuario: String, // "cliente" o "tecnico"
    val fotoPerfil: String = "",
    val disponibilidad: String = "",
    val fechaRegistro: Date,
)
