package com.jsborbon.reparalo.data.repository

import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.ForumTopic
import kotlinx.coroutines.flow.Flow

interface ForumRepository {
    suspend fun getTopics(): Flow<ApiResponse<List<ForumTopic>>>
    suspend fun getTopicById(topicId: String): Flow<ApiResponse<ForumTopic>>
    suspend fun createTopic(topic: ForumTopic): Flow<ApiResponse<Boolean>>
    suspend fun updateTopic(topic: ForumTopic): Flow<ApiResponse<Boolean>>
}
