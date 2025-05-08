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
            emit(ApiResponse.Failure(e.message ?: "Error al obtener temas"))
        }
    }

    override suspend fun createTopic(topic: ForumTopic): Flow<ApiResponse<Boolean>> = flow {
        emit(ApiResponse.Loading)
        try {
            api.createTopic(topic)
            emit(ApiResponse.Success(true))
        } catch (e: Exception) {
            emit(ApiResponse.Failure(e.message ?: "Error al crear tema"))
        }
    }
}
