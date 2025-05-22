package com.jsborbon.reparalo.data.repository.impl

import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.api.service.HistoryApiService
import com.jsborbon.reparalo.data.repository.HistoryRepository
import com.jsborbon.reparalo.models.ServiceHistoryItem
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val apiService: HistoryApiService,
) : HistoryRepository {

    override suspend fun getServiceHistory(userId: String): List<ServiceHistoryItem> {
        return apiService.getServiceHistory(userId)
    }

    override suspend fun fetchServiceById(id: String): ApiResponse<ServiceHistoryItem> {
        return try {
            val response = apiService.getServiceById(id)
            if (response.isSuccessful) {
                response.body()?.let {
                    ApiResponse.Success(it)
                } ?: ApiResponse.Failure("Respuesta vacía")
            } else {
                ApiResponse.Failure("Error: ${response.code()}")
            }
        } catch (e: Exception) {
            ApiResponse.Failure("Excepción: ${e.localizedMessage}")
        }
    }
}
