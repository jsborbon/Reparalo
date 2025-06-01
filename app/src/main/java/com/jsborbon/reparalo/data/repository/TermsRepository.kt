package com.jsborbon.reparalo.data.repository

import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.TermsAndConditions
import kotlinx.coroutines.flow.Flow

interface TermsRepository {
    fun getTermsAndConditions(): Flow<ApiResponse<TermsAndConditions>>
}
