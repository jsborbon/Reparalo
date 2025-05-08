package com.jsborbon.reparalo.data.repository.impl

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.TechnicianRepository
import com.jsborbon.reparalo.models.User
import com.jsborbon.reparalo.models.UserType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TechnicianRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : TechnicianRepository {

    private val TAG = "TechnicianRepositoryImpl"

    override fun getAllTechnicians(): Flow<ApiResponse<List<User>>> = flow {
        emit(ApiResponse.Loading)
        val snapshot = firestore.collection("users")
            .whereEqualTo("userType", UserType.TECHNICIAN.name)
            .get()
            .await()
        val list = snapshot.toObjects(User::class.java)
        emit(ApiResponse.success(list))
    }.catch { e ->
        Log.e(TAG, "getAllTechnicians error: ${e.message}", e)
        emit(ApiResponse.failure("Error loading technicians: ${e.localizedMessage}"))
    }

    override fun getTechniciansBySpecialty(
        specialty: String,
        page: Int?,
        pageSize: Int?
    ): Flow<ApiResponse<List<User>>> = flow {
        emit(ApiResponse.Loading)

        var query = firestore.collection("users")
            .whereEqualTo("userType", UserType.TECHNICIAN.name)
            .whereEqualTo("specialty", specialty)

        // Simple pagination logic
        if (page != null && pageSize != null) {
            val offset = (page - 1) * pageSize
            query = query.limit((offset + pageSize).toLong())
        }

        val snapshot = query.get().await()
        val list = snapshot.toObjects(User::class.java)

        // If paginated, trim manually
        val paginated = if (page != null && pageSize != null) {
            list.drop((page - 1) * pageSize).take(pageSize)
        } else {
            list
        }

        emit(ApiResponse.success(paginated))
    }.catch { e ->
        Log.e(TAG, "getTechniciansBySpecialty error: ${e.message}", e)
        emit(ApiResponse.failure("Error filtering technicians: ${e.localizedMessage}"))
    }

    override fun getTechnicianById(uid: String): Flow<ApiResponse<User>> = flow {
        emit(ApiResponse.Loading)
        val snapshot = firestore.collection("users").document(uid).get().await()
        val user = snapshot.toObject(User::class.java)

        if (user != null && user.userType == UserType.TECHNICIAN) {
            emit(ApiResponse.success(user))
        } else {
            emit(ApiResponse.failure("Technician not found"))
        }
    }.catch { e ->
        Log.e(TAG, "getTechnicianById error: ${e.message}", e)
        emit(ApiResponse.failure("Error retrieving technician: ${e.localizedMessage}"))
    }
}
