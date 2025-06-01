package com.jsborbon.reparalo.data.repository.impl

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.TermsRepository
import com.jsborbon.reparalo.models.TermsAndConditions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class TermsRepositoryImpl(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
) : TermsRepository {

    private val TAG = TermsRepositoryImpl::class.java.simpleName

    override fun getTermsAndConditions(): Flow<ApiResponse<TermsAndConditions>> = flow {
        emit(ApiResponse.Loading)
        try {
            val snapshot = firestore.collection("terms").document("actual").get().await()
            val terms = snapshot.toObject(TermsAndConditions::class.java)
            if (terms != null) {
                emit(ApiResponse.Success(terms))
            } else {
                emit(ApiResponse.Failure("Términos no encontrados"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "getTermsAndConditions failed", e)
            emit(ApiResponse.Failure("Error al cargar los términos y condiciones"))
        }
    }
}
