package com.jsborbon.reparalo.data.api.service

import com.jsborbon.reparalo.models.Tutorial
import retrofit2.http.GET
import retrofit2.http.Path

interface FavoritesApiService {

    @GET("users/{userId}/favorites")
    suspend fun getFavoriteTutorials(@Path("userId") userId: String): List<Tutorial>
}
