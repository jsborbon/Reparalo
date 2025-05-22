package com.jsborbon.reparalo.data.repository.impl

import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.api.service.NotificationApiService
import com.jsborbon.reparalo.data.repository.NotificationRepository
import com.jsborbon.reparalo.models.NotificationItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val apiService: NotificationApiService,
) : NotificationRepository {

    override fun getNotifications(): Flow<ApiResponse<List<NotificationItem>>> = flow {
        emit(ApiResponse.Loading)
        val response = apiService.getNotifications()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                emit(ApiResponse.Success(body))
            } else {
                emit(ApiResponse.Failure("Respuesta vacía del servidor."))
            }
        } else {
            emit(ApiResponse.Failure("Error ${response.code()}: ${response.message()}"))
        }
    }.catch { e ->
        emit(ApiResponse.Failure("Excepción: ${e.localizedMessage}"))
    }
}
