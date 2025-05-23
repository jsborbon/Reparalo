package com.jsborbon.reparalo.data.repository.impl

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.HistoryRepository
import com.jsborbon.reparalo.models.ServiceHistoryItem
import kotlinx.coroutines.tasks.await

class HistoryRepositoryImpl : HistoryRepository {

    private val db = Firebase.firestore
    private val TAG = "HistoryRepositoryImpl"
    private val userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

    override suspend fun getServiceHistory(userId: String): List<ServiceHistoryItem> {
        return try {
            val snapshot = db.collection("users")
                .document(userId)
                .collection("serviceHistory")
                .get()
                .await()

            snapshot.documents.mapNotNull { it.toObject(ServiceHistoryItem::class.java) }
        } catch (e: Exception) {
            Log.e(TAG, "getServiceHistory exception: ${e.message}", e)
            emptyList()
        }
    }

    override suspend fun fetchServiceById(id: String): ApiResponse<ServiceHistoryItem> {
        return try {
            val doc = db.collection("serviceHistory").document(id).get().await()
            val item = doc.toObject(ServiceHistoryItem::class.java)
            if (item != null) {
                ApiResponse.Success(item)
            } else {
                ApiResponse.Failure("Servicio no encontrado")
            }
        } catch (e: Exception) {
            Log.e(TAG, "fetchServiceById exception: ${e.message}", e)
            ApiResponse.Failure("Error al cargar el detalle del servicio")
        }
    }
}
