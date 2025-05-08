package com.jsborbon.reparalo.data.api.service

import com.jsborbon.reparalo.models.ForumTopic
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ForumApiService {

    @GET("forum/topics")
    suspend fun getTopics(): List<ForumTopic>

    @POST("forum/topics")
    suspend fun createTopic(@Body topic: ForumTopic)
}
