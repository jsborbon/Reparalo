package com.jsborbon.reparalo.data.repository.impl

import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.api.service.ForumApiService
import com.jsborbon.reparalo.data.repository.ForumRepository
import com.jsborbon.reparalo.models.ForumTopic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ForumRepositoryImpl @Inject constructor(
    private val api: ForumApiService
) : ForumRepository {

    override suspend fun getTopics(): Flow<ApiResponse<List<ForumTopic>>> = flow {
        emit(ApiResponse.Loading)
        try {
            val topics = api.getTopics()
            emit(ApiResponse.Success(topics))
        } catch (e: Exception) {
            emit(ApiResponse.Failure(e.message ?: "Error while fetching forum topics"))
        }
    }

    override suspend fun createTopic(topic: ForumTopic): Flow<ApiResponse<Boolean>> = flow {
        emit(ApiResponse.Loading)
        try {
            api.createTopic(topic)
            emit(ApiResponse.Success(true))
        } catch (e: Exception) {
            emit(ApiResponse.Failure(e.message ?: "Error while creating topic"))
        }
    }

    override suspend fun getTopicById(topicId: String): Flow<ApiResponse<ForumTopic>> = flow {
        emit(ApiResponse.Loading)
        try {
            val topic = api.getTopicById(topicId)
            emit(ApiResponse.Success(topic))
        } catch (e: Exception) {
            emit(ApiResponse.Failure(e.message ?: "Error while fetching topic"))
        }
    }

    override suspend fun updateTopic(
        id: String,
        title: String,
        description: String,
        category: String
    ): Flow<ApiResponse<Boolean>> = flow {
        emit(ApiResponse.Loading)
        try {
            api.updateTopic(id, title, description, category)
            emit(ApiResponse.Success(true))
        } catch (e: Exception) {
            emit(ApiResponse.Failure(e.message ?: "Error while updating topic"))
        }
    }
}
