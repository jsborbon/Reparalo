package com.jsborbon.reparalo.data

import android.util.Log
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.api.RetrofitClient
import com.jsborbon.reparalo.models.Comentario
import com.jsborbon.reparalo.models.Tecnico
import com.jsborbon.reparalo.models.Tutorial
import com.jsborbon.reparalo.models.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ReparaloRepositoryImpl @Inject constructor(
    private val apiService: RetrofitClient.ApiService,
) : DataRepository {

    override fun getTutoriales(): Flow<ApiResponse<List<Tutorial>>> = flow {
        emit(ApiResponse.loading())
        try {
            val resp = apiService.getTutoriales()
            if (resp.isSuccessful) {
                emit(ApiResponse.success(resp.body()!!))
            } else {
                emit(ApiResponse.error("Error ${resp.code()}: ${resp.message()}", resp.code()))
            }
        } catch (e: Exception) {
            Log.e("ReparaloRepo", "getTutoriales error", e)
            emit(ApiResponse.error("Conexión fallida: ${e.localizedMessage}", null))
        }
    }.flowOn(Dispatchers.IO)

    override fun getTutorialesPorCategoria(categoria: String): Flow<ApiResponse<List<Tutorial>>> = flow {
        emit(ApiResponse.loading())
        try {
            val resp = apiService.getTutorialesPorCategoria(categoria)
            if (resp.isSuccessful) {
                emit(ApiResponse.success(resp.body()!!))
            } else {
                emit(ApiResponse.error("Error ${resp.code()}: ${resp.message()}", resp.code()))
            }
        } catch (e: Exception) {
            Log.e("ReparaloRepo", "getTutorialesPorCategoria error", e)
            emit(ApiResponse.error("Conexión fallida: ${e.localizedMessage}", null))
        }
    }.flowOn(Dispatchers.IO)

    override fun getTutorialesPorCategoria(categoria: String, page: Int): Flow<ApiResponse<List<Tutorial>>> {
        // Si más adelante implementas paginación real, este método podrá usar el parámetro `page`.
        return getTutorialesPorCategoria(categoria)
    }

    override fun getTutorial(tutorialId: String): Flow<ApiResponse<Tutorial>> = flow {
        emit(ApiResponse.loading())
        try {
            val resp = apiService.getTutorial(tutorialId)
            if (resp.isSuccessful) {
                emit(ApiResponse.success(resp.body()!!))
            } else {
                emit(ApiResponse.error("Error ${resp.code()}: ${resp.message()}", resp.code()))
            }
        } catch (e: Exception) {
            Log.e("ReparaloRepo", "getTutorial error", e)
            emit(ApiResponse.error("Conexión fallida: ${e.localizedMessage}", null))
        }
    }.flowOn(Dispatchers.IO)

    override fun getUsuarioData(string: String) {
        TODO("Not yet implemented")
    }

    override fun getComentariosPorTutorial(idTutorial: String): Flow<ApiResponse<List<Comentario>>> = flow {
        emit(ApiResponse.loading())
        try {
            val resp = apiService.getComentariosPorTutorial(idTutorial)
            if (resp.isSuccessful) {
                emit(ApiResponse.success(resp.body()!!))
            } else {
                emit(ApiResponse.error("Error ${resp.code()}: ${resp.message()}", resp.code()))
            }
        } catch (e: Exception) {
            Log.e("ReparaloRepo", "getComentariosPorTutorial error", e)
            emit(ApiResponse.error("Conexión fallida: ${e.localizedMessage}", null))
        }
    }.flowOn(Dispatchers.IO)

    override fun crearComentario(comentario: Comentario): Flow<ApiResponse<Comentario>> = flow {
        emit(ApiResponse.loading())
        try {
            val resp = apiService.crearComentario(comentario)
            if (resp.isSuccessful) {
                emit(ApiResponse.success(resp.body()!!))
            } else {
                emit(ApiResponse.error("Error ${resp.code()}: ${resp.message()}", resp.code()))
            }
        } catch (e: Exception) {
            Log.e("ReparaloRepo", "crearComentario error", e)
            emit(ApiResponse.error("Conexión fallida: ${e.localizedMessage}", null))
        }
    }.flowOn(Dispatchers.IO)

    override fun getTecnicos(): Flow<ApiResponse<List<Tecnico>>> = flow {
        emit(ApiResponse.loading())
        try {
            val resp = apiService.getTecnicos()
            if (resp.isSuccessful) {
                emit(ApiResponse.success(resp.body()!!))
            } else {
                emit(ApiResponse.error("Error ${resp.code()}: ${resp.message()}", resp.code()))
            }
        } catch (e: Exception) {
            Log.e("ReparaloRepo", "getTecnicos error", e)
            emit(ApiResponse.error("Conexión fallida: ${e.localizedMessage}", null))
        }
    }.flowOn(Dispatchers.IO)

    override fun getTecnicosPorEspecialidad(especialidad: String): Flow<ApiResponse<List<Tecnico>>> = flow {
        emit(ApiResponse.loading())
        try {
            val resp = apiService.getTecnicosPorEspecialidad(especialidad)
            if (resp.isSuccessful) {
                emit(ApiResponse.success(resp.body()!!))
            } else {
                emit(ApiResponse.error("Error ${resp.code()}: ${resp.message()}", resp.code()))
            }
        } catch (e: Exception) {
            Log.e("ReparaloRepo", "getTecnicosPorEspecialidad error", e)
            emit(ApiResponse.error("Conexión fallida: ${e.localizedMessage}", null))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getUsuarioData(uid: String): Usuario? {
        return try {
            val response = apiService.getUsuario(uid)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            Log.e("ReparaloRepo", "getUsuarioData error", e)
            null
        }
    }
}
