package com.jsborbon.reparalo.data.repository.impl

import com.jsborbon.reparalo.data.api.service.HistoryApiService
import com.jsborbon.reparalo.data.repository.HistoryRepository
import com.jsborbon.reparalo.models.ServiceHistoryItem
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val apiService: HistoryApiService
) : HistoryRepository {

    override suspend fun getServiceHistory(userId: String): List<ServiceHistoryItem> {
        return apiService.getServiceHistory(userId)
    }
}
