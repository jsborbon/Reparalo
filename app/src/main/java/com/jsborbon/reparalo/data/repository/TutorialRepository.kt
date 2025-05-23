package com.jsborbon.reparalo.data.repository

import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.Tutorial
import kotlinx.coroutines.flow.Flow

interface TutorialRepository {
    fun getTutorials(): Flow<ApiResponse<List<Tutorial>>>
    fun getTutorialsByCategory(category: String): Flow<ApiResponse<List<Tutorial>>>
    fun getTutorial(id: String): Flow<ApiResponse<Tutorial>>
    fun createTutorial(tutorial: Tutorial): Flow<ApiResponse<Tutorial>>
    fun updateTutorial(id: String, tutorial: Tutorial): Flow<ApiResponse<Tutorial>>
    fun deleteTutorial(id: String): Flow<ApiResponse<Unit>>

    fun getFavoriteTutorials(): Flow<ApiResponse<List<Tutorial>>>
    fun isFavorite(tutorialId: String): Flow<ApiResponse<Boolean>>
    fun addFavorite(tutorialId: String): Flow<ApiResponse<Unit>>
    fun removeFavorite(tutorialId: String): Flow<ApiResponse<Unit>>
}
