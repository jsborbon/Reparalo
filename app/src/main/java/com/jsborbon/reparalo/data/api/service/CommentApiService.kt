package com.jsborbon.reparalo.data.api.service

import com.jsborbon.reparalo.models.Comment
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CommentApiService {

    @GET("comentarios/tutorial/{tutorialId}")
    suspend fun getByTutorial(
        @Path("tutorialId") tutorialId: String
    ): Response<List<Comment>>

    @POST("comentarios")
    suspend fun create(
        @Body comment: Comment
    ): Response<Comment>
}
