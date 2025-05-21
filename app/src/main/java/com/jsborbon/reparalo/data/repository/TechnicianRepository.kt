package com.jsborbon.reparalo.data.repository

import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.User
import kotlinx.coroutines.flow.Flow

interface TechnicianRepository {

    fun getAllTechnicians(): Flow<ApiResponse<List<User>>>

    fun getTechniciansBySpecialty(
        specialty: String,
        page: Int? = null,
        pageSize: Int? = null,
    ): Flow<ApiResponse<List<User>>>

    fun getTechnicianById(uid: String): Flow<ApiResponse<User>>
}
