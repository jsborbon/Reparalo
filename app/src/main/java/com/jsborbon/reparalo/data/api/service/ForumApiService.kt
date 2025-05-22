package com.jsborbon.reparalo.data.api.service

import com.jsborbon.reparalo.models.ForumTopic
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ForumApiService {

    @GET("forum/topics")
    suspend fun getTopics(): List<ForumTopic>

    @POST("forum/topics")
    suspend fun createTopic(@Body topic: ForumTopic): Response<Unit>

    @GET("forum/topics/{id}")
    suspend fun getTopicById(@Path("id") id: String): ForumTopic

    @PUT("forum/topics/{id}")
    suspend fun updateTopic(
        @Path("id") id: String,
        @Body topic: ForumTopic,
    ): Unit
}
