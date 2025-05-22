package com.jsborbon.reparalo.data.repository.impl

import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.api.service.FavoritesApiService
import com.jsborbon.reparalo.data.repository.FavoritesRepository
import com.jsborbon.reparalo.models.Tutorial
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(
    private val apiService: FavoritesApiService,
) : FavoritesRepository {

    override fun getFavoriteTutorials(userId: String): Flow<ApiResponse<List<Tutorial>>> = flow {
        emit(ApiResponse.Loading)
        try {
            val favorites = apiService.getFavoriteTutorials(userId)
            emit(ApiResponse.Success(favorites))
        } catch (e: Exception) {
            emit(ApiResponse.Failure(e.message ?: "Error al obtener favoritos"))
        }
    }

    override fun addFavorite(tutorialId: String): Flow<ApiResponse<Boolean>> = flow {
        emit(ApiResponse.Loading)
        try {
            val response = apiService.addFavorite(tutorialId)
            if (response.isSuccessful) {
                emit(ApiResponse.Success(true))
            } else {
                emit(ApiResponse.Failure("Error ${response.code()}: no se pudo añadir a favoritos"))
            }
        } catch (e: Exception) {
            emit(ApiResponse.Failure(e.message ?: "Error al añadir a favoritos"))
        }
    }

    override fun removeFavorite(tutorialId: String): Flow<ApiResponse<Boolean>> = flow {
        emit(ApiResponse.Loading)
        try {
            val response = apiService.removeFavorite(tutorialId)
            if (response.isSuccessful) {
                emit(ApiResponse.Success(true))
            } else {
                emit(ApiResponse.Failure("Error ${response.code()}: no se pudo eliminar de favoritos"))
            }
        } catch (e: Exception) {
            emit(ApiResponse.Failure(e.message ?: "Error al eliminar de favoritos"))
        }
    }
}
