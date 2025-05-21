package com.jsborbon.reparalo.data.api.service

import com.jsborbon.reparalo.models.ServiceHistoryItem
import retrofit2.http.GET
import retrofit2.http.Path

interface HistoryApiService {
    @GET("users/{userId}/history")
    suspend fun getServiceHistory(
        @Path("userId") userId: String,
    ): List<ServiceHistoryItem>
}
