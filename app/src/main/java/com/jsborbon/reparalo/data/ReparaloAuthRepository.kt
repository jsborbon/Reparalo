package com.jsborbon.reparalo.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.jsborbon.reparalo.data.api.RetrofitClient
import com.jsborbon.reparalo.models.Usuario
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject

class ReparaloAuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
) : AuthRepository {

    private val apiService = RetrofitClient.apiService

    override suspend fun signIn(email: String, password: String): FirebaseUser? =
        try {
            auth.signInWithEmailAndPassword(email, password).await().user
        } catch (e: Exception) {
            Log.e("AuthRepo", "signIn error: ${e.message}")
            null
        }

    override suspend fun signUp(
        email: String,
        password: String,
        nombre: String,
        telefono: String,
        tipoUsuario: String,
    ): FirebaseUser? =
        try {
            val user = auth.createUserWithEmailAndPassword(email, password).await().user
            user?.let {
                // Paso 1: Crear el usuario en FirebaseAuth (ya hecho)
                // Paso 2: Enviar los datos a tu backend Spring con Retrofit
                val usuario = Usuario(
                    uid = it.uid,
                    nombre = nombre,
                    email = email,
                    telefono = telefono,
                    tipoUsuario = tipoUsuario,
                    disponibilidad = "No especificada",
                    fechaRegistro = Date(),
                )
                val response = apiService.actualizarUsuario(it.uid, usuario)
                if (!response.isSuccessful) {
                    Log.e("AuthRepo", "Error al registrar usuario en backend: ${response.message()}")
                }
            }
            user
        } catch (e: Exception) {
            Log.e("AuthRepo", "signUp error: ${e.message}")
            null
        }

    override fun signOut() {
        auth.signOut()
    }

    override suspend fun getUsuarioData(uid: String): Usuario? =
        try {
            val response = apiService.getUsuario(uid)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            Log.e("AuthRepo", "getUsuarioData error: ${e.message}")
            null
        }

    override suspend fun sendPasswordReset(email: String): Boolean =
        try {
            FirebaseAuth.getInstance().sendPasswordResetEmail(email).await()
            true
        } catch (e: Exception) {
            false
        }
}
