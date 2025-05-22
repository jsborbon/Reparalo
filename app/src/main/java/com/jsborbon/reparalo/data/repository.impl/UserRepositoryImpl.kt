package com.jsborbon.reparalo.data.repository.impl

import android.util.Log
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.api.service.UserApiService
import com.jsborbon.reparalo.data.repository.UserRepository
import com.jsborbon.reparalo.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: UserApiService,
) : UserRepository {

    private val TAG = "UserRepositoryImpl"

    override suspend fun getUserData(uid: String): User? {
        return try {
            val response = apiService.getUser(uid)
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e(TAG, "getUserData failed: ${response.code()} - ${response.message()}")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "getUserData exception: ${e.message}", e)
            null
        }
    }

    override fun updateUser(user: User): Flow<ApiResponse<User>> = flow {
        emit(ApiResponse.Loading)
        val response = apiService.updateUser(user.uid, user)
        if (response.isSuccessful) {
            response.body()?.let {
                emit(ApiResponse.Success(it))
            } ?: emit(ApiResponse.Failure("Empty response body"))
        } else {
            emit(ApiResponse.Failure("Error ${response.code()}: ${response.message()}"))
        }
    }.catch { e ->
        Log.e(TAG, "updateUser exception: ${e.message}", e)
        emit(ApiResponse.Failure("Exception: ${e.localizedMessage}"))
    }
}
