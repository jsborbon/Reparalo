package com.jsborbon.reparalo.data

import android.util.Log
import com.jsborbon.reparalo.data.api.RetrofitClient
import com.jsborbon.reparalo.models.Usuario
import retrofit2.Response
import javax.inject.Inject

class UsuarioRepository @Inject constructor() {

    private val apiService = RetrofitClient.apiService

    suspend fun getUsuario(uid: String): Usuario? =
        try {
            val response = apiService.getUsuario(uid)
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("UsuarioRepo", "Error ${response.code()}: ${response.message()}")
                null
            }
        } catch (e: Exception) {
            Log.e("UsuarioRepo", "getUsuario error: ${e.message}")
            null
        }

    suspend fun actualizarUsuario(uid: String, usuario: Usuario): Boolean =
        try {
            val response: Response<Usuario> = apiService.actualizarUsuario(uid, usuario)
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("UsuarioRepo", "actualizarUsuario error: ${e.message}")
            false
        }
}
