package com.jsborbon.reparalo.data.repository

import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.ServiceHistoryItem
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun getServiceHistory(userId: String): Flow<ApiResponse<List<ServiceHistoryItem>>>
    fun fetchServiceById(id: String): Flow<ApiResponse<ServiceHistoryItem>>
}
