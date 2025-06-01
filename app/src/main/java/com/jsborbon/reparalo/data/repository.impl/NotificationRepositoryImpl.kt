package com.jsborbon.reparalo.data.repository.impl

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.NotificationRepository
import com.jsborbon.reparalo.models.NotificationItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class NotificationRepositoryImpl(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
) : NotificationRepository {

    private val TAG = NotificationRepositoryImpl::class.java.simpleName

    override fun getNotifications(): Flow<ApiResponse<List<NotificationItem>>> = flow {
        emit(ApiResponse.Loading)
        val snapshot = firestore.collection("notifications").get().await()
        val notifications = snapshot.documents.mapNotNull { it.toObject(NotificationItem::class.java) }
        emit(ApiResponse.Success(notifications))
    }.catch { e ->
        Log.e(TAG, "getNotifications failed", e)
        emit(ApiResponse.Failure("Error al cargar las notificaciones"))
    }
}
