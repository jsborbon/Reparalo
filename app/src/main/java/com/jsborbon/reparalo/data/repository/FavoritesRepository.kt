package com.jsborbon.reparalo.data.repository

import com.jsborbon.reparalo.models.Tutorial

interface FavoritesRepository {
    suspend fun getFavoriteTutorials(userId: String): List<Tutorial>
}
