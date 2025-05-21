package com.jsborbon.reparalo.data.api.service

import com.jsborbon.reparalo.models.ForumTopic
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ForumApiService {

    @GET("forum/topics")
    suspend fun getTopics(): List<ForumTopic>

    @POST("forum/topics")
    suspend fun createTopic(@Body topic: ForumTopic)

    @GET("forum/topics/{id}")
    fun getTopicById(@Path("id") id: String): ForumTopic

    @PUT("forum/topics/{id}")
    fun updateTopic(
        @Path("id") id: String,
        @Body title: String,
        @Body description: String,
        @Body category: String
    )
}
