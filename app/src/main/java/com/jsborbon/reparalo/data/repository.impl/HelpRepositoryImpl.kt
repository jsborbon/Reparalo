package com.jsborbon.reparalo.data.repository.impl

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.HelpRepository
import com.jsborbon.reparalo.models.HelpItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class HelpRepositoryImpl(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
) : HelpRepository {

    private val TAG = HelpRepositoryImpl::class.java.simpleName

    override fun getHelpItems(): Flow<ApiResponse<List<HelpItem>>> = flow {
        emit(ApiResponse.Loading)
        try {
            val snapshot = firestore.collection("helpItems").get().await()
            val helpItems = snapshot.documents.mapNotNull { it.toObject(HelpItem::class.java) }
            emit(ApiResponse.Success(helpItems))
        } catch (e: Exception) {
            Log.e(TAG, "getHelpItems failed", e)
            emit(ApiResponse.Failure("Error al cargar los elementos de ayuda"))
        }
    }
}
