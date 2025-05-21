package com.jsborbon.reparalo.data.repository

import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.Material
import kotlinx.coroutines.flow.Flow

interface MaterialRepository {
    suspend fun getMaterials(): Flow<ApiResponse<List<Material>>>
    suspend fun getMaterialById(id: String): ApiResponse<Material>
    suspend fun updateMaterial(material: Material): Flow<ApiResponse<Material>>
}
