package com.jsborbon.reparalo.data.repository.impl

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.HelpRepository
import com.jsborbon.reparalo.models.HelpItem
import kotlinx.coroutines.tasks.await

class HelpRepositoryImpl : HelpRepository {

    private val db = Firebase.firestore
    private val TAG = "HelpRepositoryImpl"

    override suspend fun getHelpItems(): ApiResponse<List<HelpItem>> {
        return try {
            val snapshot = db.collection("helpItems").get().await()
            val items = snapshot.documents.mapNotNull { it.toObject(HelpItem::class.java) }
            ApiResponse.Success(items)
        } catch (e: Exception) {
            Log.e(TAG, "getHelpItems exception: ${e.message}", e)
            ApiResponse.Failure("Error al cargar los elementos de ayuda")
        }
    }
}
