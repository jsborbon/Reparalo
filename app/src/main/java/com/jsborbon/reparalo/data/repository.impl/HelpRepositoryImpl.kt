package com.jsborbon.reparalo.data.repository.impl

import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.api.service.HelpApiService
import com.jsborbon.reparalo.data.repository.HelpRepository
import com.jsborbon.reparalo.models.HelpItem
import javax.inject.Inject

class HelpRepositoryImpl @Inject constructor(
    private val apiService: HelpApiService,
) : HelpRepository {

    override suspend fun getHelpItems(): ApiResponse<List<HelpItem>> {
        return try {
            val response = apiService.fetchHelpItems()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    ApiResponse.Success(body)
                } else {
                    ApiResponse.Failure("Response body is null")
                }
            } else {
                ApiResponse.Failure("Error: ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            ApiResponse.Failure(e.message ?: "Unknown error")
        }
    }
}
