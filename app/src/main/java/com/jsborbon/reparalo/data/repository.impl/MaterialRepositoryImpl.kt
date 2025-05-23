package com.jsborbon.reparalo.data.repository.impl

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.MaterialRepository
import com.jsborbon.reparalo.models.Material
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class MaterialRepositoryImpl : MaterialRepository {

    private val db = Firebase.firestore
    private val TAG = "MaterialRepositoryImpl"

    override suspend fun getMaterials(): Flow<ApiResponse<List<Material>>> = flow {
        emit(ApiResponse.Loading)
        val snapshot = db.collection("materials").get().await()
        val materials = snapshot.documents.mapNotNull { it.toObject(Material::class.java) }
        emit(ApiResponse.Success(materials))
    }.catch { e ->
        Log.e(TAG, "getMaterials exception: ${e.message}", e)
        emit(ApiResponse.Failure("Error al obtener materiales"))
    }

    override suspend fun getMaterialById(id: String): ApiResponse<Material> {
        return try {
            val doc = db.collection("materials").document(id).get().await()
            val material = doc.toObject(Material::class.java)
            if (material != null) {
                ApiResponse.Success(material)
            } else {
                ApiResponse.Failure("Material no encontrado")
            }
        } catch (e: Exception) {
            Log.e(TAG, "getMaterialById exception: ${e.message}", e)
            ApiResponse.Failure("Error al obtener el material")
        }
    }

    override suspend fun updateMaterial(material: Material): Flow<ApiResponse<Material>> = flow {
        emit(ApiResponse.Loading)
        db.collection("materials").document(material.id).set(material).await()
        emit(ApiResponse.Success(material))
    }.catch { e ->
        Log.e(TAG, "updateMaterial exception: ${e.message}", e)
        emit(ApiResponse.Failure("Error al actualizar el material"))
    }
}
