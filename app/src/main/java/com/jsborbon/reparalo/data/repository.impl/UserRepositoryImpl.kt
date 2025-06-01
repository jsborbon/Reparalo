package com.jsborbon.reparalo.data.repository.impl

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.UserRepository
import com.jsborbon.reparalo.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
) : UserRepository {

    private val TAG = UserRepositoryImpl::class.java.simpleName

    override fun getUserData(uid: String): Flow<ApiResponse<User>> = flow {
        emit(ApiResponse.Loading)
        val snapshot = firestore.collection("users").document(uid).get().await()
        val user = snapshot.toObject(User::class.java)
        if (user != null) {
            emit(ApiResponse.Success(user))
        } else {
            emit(ApiResponse.Failure("Usuario no encontrado"))
        }
    }.catch { e ->
        Log.e(TAG, "getUserData exception: ${e.message}", e)
        emit(ApiResponse.Failure("Error al obtener los datos del usuario"))
    }

    override fun updateUser(user: User): Flow<ApiResponse<User>> = flow {
        emit(ApiResponse.Loading)
        firestore.collection("users").document(user.uid).set(user).await()
        emit(ApiResponse.Success(user))
    }.catch { e ->
        Log.e(TAG, "updateUser exception: ${e.message}", e)
        emit(ApiResponse.Failure("Error al actualizar el usuario"))
    }
}
