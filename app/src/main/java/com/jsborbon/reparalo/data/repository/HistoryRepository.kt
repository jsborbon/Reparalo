package com.jsborbon.reparalo.data.repository

import com.jsborbon.reparalo.models.ServiceHistoryItem

interface HistoryRepository {
    suspend fun getServiceHistory(userId: String): List<ServiceHistoryItem>
}
