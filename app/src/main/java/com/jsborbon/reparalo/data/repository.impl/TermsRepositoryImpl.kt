package com.jsborbon.reparalo.data.repository.impl

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.TermsRepository
import com.jsborbon.reparalo.models.TermsAndConditions
import kotlinx.coroutines.tasks.await

class TermsRepositoryImpl : TermsRepository {

    private val db = Firebase.firestore
    private val TAG = "TermsRepositoryImpl"

    override suspend fun getTermsAndConditions(): ApiResponse<TermsAndConditions> {
        return try {
            val snapshot = db.collection("terms").document("actual").get().await()
            val terms = snapshot.toObject(TermsAndConditions::class.java)
            if (terms != null) {
                ApiResponse.Success(terms)
            } else {
                ApiResponse.Failure("Términos no encontrados")
            }
        } catch (e: Exception) {
            Log.e(TAG, "getTermsAndConditions exception: ${e.message}", e)
            ApiResponse.Failure("Error al cargar los términos y condiciones")
        }
    }
}
