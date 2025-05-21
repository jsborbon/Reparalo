package com.jsborbon.reparalo.data.api.service

import com.jsborbon.reparalo.models.Tutorial
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TutorialApiService {
    @GET("tutoriales")
    suspend fun getAll(): Response<List<Tutorial>>

    @GET("tutoriales/categoria/{categoria}")
    suspend fun getByCategory(@Path("categoria") category: String): Response<List<Tutorial>>

    @GET("tutoriales/{id}")
    suspend fun getById(@Path("id") id: String): Response<Tutorial>

    @POST("tutoriales")
    suspend fun create(@Body tutorial: Tutorial): Response<Tutorial>

    @PUT("tutoriales/{id}")
    suspend fun update(@Path("id") id: String, @Body tutorial: Tutorial): Response<Tutorial>

    @DELETE("tutoriales/{id}")
    suspend fun delete(@Path("id") id: String): Response<Unit>

    @GET("tutoriales/{id}/favorite")
    suspend fun isFavorite(@Path("id") tutorialId: String): Response<Boolean>

    @POST("tutoriales/{id}/favorite")
    suspend fun addFavorite(@Path("id") tutorialId: String): Response<Unit>

    @DELETE("tutoriales/{id}/favorite")
    suspend fun removeFavorite(@Path("id") tutorialId: String): Response<Unit>

}
