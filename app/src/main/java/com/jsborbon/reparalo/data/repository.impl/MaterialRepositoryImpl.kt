package com.jsborbon.reparalo.data.repository.impl

import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.api.service.MaterialApiService
import com.jsborbon.reparalo.data.repository.MaterialRepository
import com.jsborbon.reparalo.models.Material
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MaterialRepositoryImpl @Inject constructor(
    private val api: MaterialApiService,
) : MaterialRepository {

    override suspend fun getMaterials(): Flow<ApiResponse<List<Material>>> = flow {
        emit(ApiResponse.Loading)
        try {
            val materials = api.getMaterials()
            emit(ApiResponse.Success(materials))
        } catch (e: Exception) {
            emit(ApiResponse.Failure(e.message ?: "Error al obtener materiales"))
        }
    }

    override suspend fun getMaterialById(id: String): ApiResponse<Material> {
        return try {
            val material = api.getMaterialById(id)
            ApiResponse.Success(material)
        } catch (e: Exception) {
            ApiResponse.Failure(e.message ?: "Error al obtener el material")
        }
    }

    override suspend fun updateMaterial(material: Material): Flow<ApiResponse<Material>> = flow {
        emit(ApiResponse.Loading)
        try {
            val updated = api.updateMaterial(material.id, material)
            emit(ApiResponse.Success(updated))
        } catch (e: Exception) {
            emit(ApiResponse.Failure(e.message ?: "Error al actualizar el material"))
        }
    }
}
