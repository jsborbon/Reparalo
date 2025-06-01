package com.jsborbon.reparalo.data.repository.impl

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.MaterialRepository
import com.jsborbon.reparalo.models.Material
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class MaterialRepositoryImpl(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
) : MaterialRepository {

    private val TAG = MaterialRepositoryImpl::class.java.simpleName

    override fun getMaterials(): Flow<ApiResponse<List<Material>>> = flow {
        emit(ApiResponse.Loading)
        val snapshot = firestore.collection("materials").get().await()
        val materials = snapshot.documents.mapNotNull { it.toObject(Material::class.java) }
        emit(ApiResponse.Success(materials))
    }.catch { e ->
        Log.e(TAG, "getMaterials failed", e)
        emit(ApiResponse.Failure("Error al obtener materiales"))
    }

    override fun getMaterialById(id: String): Flow<ApiResponse<Material>> = flow {
        emit(ApiResponse.Loading)
        val document = firestore.collection("materials").document(id).get().await()
        val material = document.toObject(Material::class.java)
        if (material != null) {
            emit(ApiResponse.Success(material))
        } else {
            emit(ApiResponse.Failure("Material no encontrado"))
        }
    }.catch { e ->
        Log.e(TAG, "getMaterialById failed", e)
        emit(ApiResponse.Failure("Error al obtener el material"))
    }

    override fun updateMaterial(material: Material): Flow<ApiResponse<Material>> = flow {
        emit(ApiResponse.Loading)
        firestore.collection("materials").document(material.id).set(material).await()
        emit(ApiResponse.Success(material))
    }.catch { e ->
        Log.e(TAG, "updateMaterial failed", e)
        emit(ApiResponse.Failure("Error al actualizar el material"))
    }
}
