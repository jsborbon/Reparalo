package com.jsborbon.reparalo.data.repository

import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getCategories(): Flow<ApiResponse<List<Category>>>
}
