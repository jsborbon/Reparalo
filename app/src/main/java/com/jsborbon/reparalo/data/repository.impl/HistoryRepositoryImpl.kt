package com.jsborbon.reparalo.data.repository.impl

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.HistoryRepository
import com.jsborbon.reparalo.models.ServiceHistoryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class HistoryRepositoryImpl(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
) : HistoryRepository {

    private val TAG = HistoryRepositoryImpl::class.java.simpleName
    private val collection = firestore.collection("serviceHistory")

    override fun getServiceHistory(userId: String): Flow<ApiResponse<List<ServiceHistoryItem>>> = flow {
        emit(ApiResponse.Loading)
        try {
            val snapshot = collection
                .whereEqualTo("userId", userId)
                .get()
                .await()

            val list = snapshot.documents
                .mapNotNull { it.toObject(ServiceHistoryItem::class.java) }

            emit(ApiResponse.Success(list))
        } catch (e: Exception) {
            Log.e(TAG, "getServiceHistory failed: ${e.message}", e)
            emit(ApiResponse.Failure("Error al cargar el historial de servicios."))
        }
    }.catch { e ->
        Log.e(TAG, "getServiceHistory catch: ${e.message}", e)
        emit(ApiResponse.Failure("Error inesperado al cargar el historial"))
    }

    override fun fetchServiceById(id: String): Flow<ApiResponse<ServiceHistoryItem>> = flow {
        emit(ApiResponse.Loading)
        try {
            val snapshot = collection
                .document(id)
                .get()
                .await()

            val item = snapshot.toObject(ServiceHistoryItem::class.java)
            if (item != null) {
                emit(ApiResponse.Success(item))
            } else {
                emit(ApiResponse.Failure("Servicio no encontrado"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "fetchServiceById failed: ${e.message}", e)
            emit(ApiResponse.Failure("Error al cargar el detalle del servicio"))
        }
    }.catch { e ->
        Log.e(TAG, "fetchServiceById catch: ${e.message}", e)
        emit(ApiResponse.Failure("Error inesperado al obtener detalle"))
    }
}
