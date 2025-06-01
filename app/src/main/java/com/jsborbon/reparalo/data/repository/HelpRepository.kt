package com.jsborbon.reparalo.data.repository

import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.HelpItem
import kotlinx.coroutines.flow.Flow

interface HelpRepository {
    fun getHelpItems(): Flow<ApiResponse<List<HelpItem>>>
}
