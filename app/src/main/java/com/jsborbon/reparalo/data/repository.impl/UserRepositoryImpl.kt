package com.jsborbon.reparalo.data.repository.impl

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.UserRepository
import com.jsborbon.reparalo.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl : UserRepository {

    private val db = Firebase.firestore
    private val TAG = "UserRepositoryImpl"

    override suspend fun getUserData(uid: String): User? {
        return try {
            val snapshot = db.collection("users").document(uid).get().await()
            snapshot.toObject(User::class.java)
        } catch (e: Exception) {
            Log.e(TAG, "getUserData exception: ${e.message}", e)
            null
        }
    }

    override fun updateUser(user: User): Flow<ApiResponse<User>> = flow {
        emit(ApiResponse.Loading)
        db.collection("users").document(user.uid).set(user).await()
        emit(ApiResponse.Success(user))
    }.catch { e ->
        Log.e(TAG, "updateUser exception: ${e.message}", e)
        emit(ApiResponse.Failure("Error al actualizar el usuario"))
    }
}
