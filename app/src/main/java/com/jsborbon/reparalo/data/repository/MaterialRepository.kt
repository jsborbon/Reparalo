package com.jsborbon.reparalo.data.repository

import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.Material
import kotlinx.coroutines.flow.Flow

interface MaterialRepository {
    fun getMaterials(): Flow<ApiResponse<List<Material>>>
    fun getMaterialById(materialID: String): Flow<ApiResponse<Material>>
    fun updateMaterial(material: Material): Flow<ApiResponse<Material>>
}
