package com.jsborbon.reparalo.data.repository

import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.Tutorial
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun getFavoriteTutorials(userId: String): Flow<ApiResponse<List<Tutorial>>>
    fun addFavorite(tutorialId: String): Flow<ApiResponse<Boolean>>
    fun removeFavorite(tutorialId: String): Flow<ApiResponse<Boolean>>
}
