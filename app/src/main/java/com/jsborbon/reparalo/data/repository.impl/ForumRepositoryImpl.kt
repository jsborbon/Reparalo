package com.jsborbon.reparalo.data.repository.impl

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.ForumRepository
import com.jsborbon.reparalo.models.ForumTopic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class ForumRepositoryImpl : ForumRepository {

    private val db = Firebase.firestore
    private val TAG = "ForumRepositoryImpl"

    override suspend fun getTopics(): Flow<ApiResponse<List<ForumTopic>>> = flow {
        emit(ApiResponse.Loading)
        val snapshot = db.collection("forumTopics").get().await()
        val topics = snapshot.documents.mapNotNull { it.toObject(ForumTopic::class.java) }
        emit(ApiResponse.Success(topics))
    }.catch { e ->
        Log.e(TAG, "getTopics exception: ${e.message}", e)
        emit(ApiResponse.Failure("Error al obtener los temas del foro"))
    }

    override suspend fun createTopic(topic: ForumTopic): Flow<ApiResponse<Boolean>> = flow {
        emit(ApiResponse.Loading)
        try {
            db.collection("forumTopics").document(topic.id).set(topic).await()
            emit(ApiResponse.Success(true))
        } catch (e: Exception) {
            Log.e(TAG, "createTopic exception: ${e.message}", e)
            emit(ApiResponse.Failure("Error al crear el tema"))
        }
    }

    override suspend fun getTopicById(topicId: String): Flow<ApiResponse<ForumTopic>> = flow {
        emit(ApiResponse.Loading)
        val snapshot = db.collection("forumTopics").document(topicId).get().await()
        val topic = snapshot.toObject(ForumTopic::class.java)
        if (topic != null) {
            emit(ApiResponse.Success(topic))
        } else {
            emit(ApiResponse.Failure("Tema no encontrado"))
        }
    }.catch { e ->
        Log.e(TAG, "getTopicById exception: ${e.message}", e)
        emit(ApiResponse.Failure("Error al obtener el tema"))
    }

    override suspend fun updateTopic(topic: ForumTopic): Flow<ApiResponse<Boolean>> = flow {
        emit(ApiResponse.Loading)
        try {
            db.collection("forumTopics").document(topic.id).set(topic).await()
            emit(ApiResponse.Success(true))
        } catch (e: Exception) {
            Log.e(TAG, "updateTopic exception: ${e.message}", e)
            emit(ApiResponse.Failure("Error al actualizar el tema"))
        }
    }
}
