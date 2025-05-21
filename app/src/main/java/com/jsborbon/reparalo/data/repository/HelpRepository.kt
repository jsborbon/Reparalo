package com.jsborbon.reparalo.data.repository

import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.HelpItem

interface HelpRepository {
    suspend fun getHelpItems(): ApiResponse<List<HelpItem>>
}
