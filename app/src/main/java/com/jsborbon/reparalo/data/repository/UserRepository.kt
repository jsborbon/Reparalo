package com.jsborbon.reparalo.data.repository

import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserData(uid: String): Flow<ApiResponse<User>>
    fun updateUser(user: User): Flow<ApiResponse<User>>
}
