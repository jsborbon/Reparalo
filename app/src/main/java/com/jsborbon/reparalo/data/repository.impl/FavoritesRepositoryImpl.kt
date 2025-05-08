package com.jsborbon.reparalo.data.repository.impl

import com.jsborbon.reparalo.data.api.service.FavoritesApiService
import com.jsborbon.reparalo.data.repository.FavoritesRepository
import com.jsborbon.reparalo.models.Tutorial
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(
    private val apiService: FavoritesApiService
) : FavoritesRepository {

    override suspend fun getFavoriteTutorials(userId: String): List<Tutorial> {
        return apiService.getFavoriteTutorials(userId)
    }
}
