package com.jsborbon.reparalo.data.repository.impl

import android.util.Log
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.api.service.UserApiService
import com.jsborbon.reparalo.data.repository.TechnicianRepository
import com.jsborbon.reparalo.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TechnicianRepositoryImpl @Inject constructor(
    private val api: UserApiService
) : TechnicianRepository {

    private val TAG = "TechnicianRepositoryImpl"

    override fun getAllTechnicians(): Flow<ApiResponse<List<User>>> = flow {
        emit(ApiResponse.Loading)
        val response = api.getAllTechnicians()
        if (response.isSuccessful) {
            emit(ApiResponse.Success(response.body().orEmpty()))
        } else {
            emit(ApiResponse.Failure("Error ${response.code()}: ${response.message()}"))
        }
    }.catch { e ->
        Log.e(TAG, "getAllTechnicians failed", e)
        emit(ApiResponse.Failure("Error loading technicians: ${e.localizedMessage}"))
    }

    override fun getTechniciansBySpecialty(
        specialty: String,
        page: Int?,
        pageSize: Int?
    ): Flow<ApiResponse<List<User>>> = flow {
        emit(ApiResponse.Loading)
        val response = api.getTechniciansBySpecialty(specialty, page, pageSize)
        if (response.isSuccessful) {
            emit(ApiResponse.Success(response.body().orEmpty()))
        } else {
            emit(ApiResponse.Failure("Error ${response.code()}: ${response.message()}"))
        }
    }.catch { e ->
        Log.e(TAG, "getTechniciansBySpecialty failed", e)
        emit(ApiResponse.Failure("Error filtering technicians: ${e.localizedMessage}"))
    }

    override fun getTechnicianById(uid: String): Flow<ApiResponse<User>> = flow {
        emit(ApiResponse.Loading)
        val response = api.getUser(uid)
        if (response.isSuccessful) {
            val user = response.body()
            if (user != null && user.userType.name == "TECHNICIAN") {
                emit(ApiResponse.Success(user))
            } else {
                emit(ApiResponse.Failure("El usuario no es un técnico válido"))
            }
        } else {
            emit(ApiResponse.Failure("Error ${response.code()}: ${response.message()}"))
        }
    }.catch { e ->
        Log.e(TAG, "getTechnicianById failed", e)
        emit(ApiResponse.Failure("Error retrieving technician: ${e.localizedMessage}"))
    }
}
