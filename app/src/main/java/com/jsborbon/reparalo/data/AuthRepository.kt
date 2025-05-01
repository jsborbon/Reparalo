package com.jsborbon.reparalo.data

import com.google.firebase.auth.FirebaseUser
import com.jsborbon.reparalo.models.Usuario

interface AuthRepository {

    suspend fun signIn(email: String, password: String): FirebaseUser?

    suspend fun signUp(
        email: String,
        password: String,
        nombre: String,
        telefono: String,
        tipoUsuario: String,
    ): FirebaseUser?

    fun signOut()

    suspend fun getUsuarioData(uid: String): Usuario?

    suspend fun sendPasswordReset(email: String): Boolean
}
