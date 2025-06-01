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

class TechnicianRepositoryImpl(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
) : TechnicianRepository {

    private val TAG = TechnicianRepositoryImpl::class.java.simpleName

    override fun getAllTechnicians(): Flow<ApiResponse<List<User>>> = flow {
        emit(ApiResponse.Loading)
        val snapshot = firestore.collection("users")
            .whereEqualTo("userType", "TECHNICIAN")
            .get()
            .await()
        val technicians = snapshot.documents.mapNotNull { it.toObject(User::class.java) }
        emit(ApiResponse.Success(technicians))
    }.catch { e ->
        Log.e(TAG, "getAllTechnicians failed", e)
        emit(ApiResponse.Failure("Error al cargar los técnicos: ${e.localizedMessage}"))
    }

    override fun getTechniciansBySpecialty(
        specialty: String,
        page: Int?,
        pageSize: Int?,
    ): Flow<ApiResponse<List<User>>> = flow {
        emit(ApiResponse.Loading)
        var query = firestore.collection("users")
            .whereEqualTo("userType", "TECHNICIAN")
            .whereEqualTo("specialty", specialty)

        if (page != null && pageSize != null) {
            query = query.limit((page * pageSize).toLong())
        }

        val snapshot = query.get().await()
        val technicians = snapshot.documents.mapNotNull { it.toObject(User::class.java) }
        emit(ApiResponse.Success(technicians))
    }.catch { e ->
        Log.e(TAG, "getTechniciansBySpecialty failed", e)
        emit(ApiResponse.Failure("Error al filtrar técnicos: ${e.localizedMessage}"))
    }

    override fun getTechnicianById(uid: String): Flow<ApiResponse<User>> = flow {
        emit(ApiResponse.Loading)
        val snapshot = firestore.collection("users").document(uid).get().await()
        val user = snapshot.toObject(User::class.java)
        if (user != null && user.userType == UserType.TECHNICIAN) {
            emit(ApiResponse.Success(user))
        } else {
            emit(ApiResponse.Failure("El usuario no es un técnico válido"))
        }
    }.catch { e ->
        Log.e(TAG, "getTechnicianById failed", e)
        emit(ApiResponse.Failure("Error al obtener el técnico: ${e.localizedMessage}"))
    }
}
