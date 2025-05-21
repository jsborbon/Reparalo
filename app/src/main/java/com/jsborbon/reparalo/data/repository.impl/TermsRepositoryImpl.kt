package com.jsborbon.reparalo.data.repository.impl

import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.api.service.TermsApiService
import com.jsborbon.reparalo.data.repository.TermsRepository
import com.jsborbon.reparalo.models.TermsAndConditions
import javax.inject.Inject

class TermsRepositoryImpl @Inject constructor(
    private val apiService: TermsApiService,
) : TermsRepository {

    override suspend fun getTermsAndConditions(): ApiResponse<TermsAndConditions> {
        return try {
            val response = apiService.fetchTerms()
            if (response.isSuccessful) {
                ApiResponse.Success(response.body()!!)
            } else {
                ApiResponse.Failure("Error al cargar términos: ${response.message()}")
            }
        } catch (e: Exception) {
            ApiResponse.Failure(e.message ?: "Error desconocido")
        }
    }
}
