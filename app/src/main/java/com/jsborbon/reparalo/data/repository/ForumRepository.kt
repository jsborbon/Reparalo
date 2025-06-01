package com.jsborbon.reparalo.data.repository

import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.ForumTopic
import kotlinx.coroutines.flow.Flow

interface ForumRepository {
    fun getTopics(): Flow<ApiResponse<List<ForumTopic>>>
    fun getTopicById(topicId: String): Flow<ApiResponse<ForumTopic>>
    fun createTopic(topic: ForumTopic): Flow<ApiResponse<Unit>>
    fun updateTopic(topic: ForumTopic): Flow<ApiResponse<Unit>>
}
