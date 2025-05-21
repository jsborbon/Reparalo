package com.jsborbon.reparalo.data.repository

import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.TermsAndConditions

interface TermsRepository {
    suspend fun getTermsAndConditions(): ApiResponse<TermsAndConditions>
}
