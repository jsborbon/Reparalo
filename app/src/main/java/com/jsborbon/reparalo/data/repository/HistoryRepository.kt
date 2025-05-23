package com.jsborbon.reparalo.data.repository

import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.ServiceHistoryItem

interface HistoryRepository {
    suspend fun getServiceHistory(): List<ServiceHistoryItem>
    suspend fun fetchServiceById(id: String): ApiResponse<ServiceHistoryItem>
}
