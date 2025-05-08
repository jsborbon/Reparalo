package com.jsborbon.reparalo.data.repository

import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUserData(uid: String): User?
    fun updateUser(user: User): Flow<ApiResponse<User>>
}
