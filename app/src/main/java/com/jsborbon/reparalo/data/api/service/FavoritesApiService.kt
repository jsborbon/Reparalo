package com.jsborbon.reparalo.data.api.service

import com.jsborbon.reparalo.models.Tutorial
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FavoritesApiService {

    @GET("users/{userId}/favorites")
    suspend fun getFavoriteTutorials(@Path("userId") userId: String): List<Tutorial>

    @POST("favorites/{tutorialId}")
    suspend fun addFavorite(@Path("tutorialId") id: String): Response<Unit>

    @DELETE("favorites/{tutorialId}")
    suspend fun removeFavorite(@Path("tutorialId") id: String): Response<Unit>
}
