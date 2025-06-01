package com.jsborbon.reparalo.data.repository.impl

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.ForumRepository
import com.jsborbon.reparalo.models.ForumTopic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class ForumRepositoryImpl(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
) : ForumRepository {

    private val TAG = ForumRepositoryImpl::class.java.simpleName

    override fun getTopics(): Flow<ApiResponse<List<ForumTopic>>> = flow {
        emit(ApiResponse.Loading)
        try {
            val snapshot = firestore.collection("forumTopics").get().await()
            val topics = snapshot.documents.mapNotNull { it.toObject(ForumTopic::class.java) }
            emit(ApiResponse.Success(topics))
        } catch (e: Exception) {
            Log.e(TAG, "getTopics failed", e)
            emit(ApiResponse.Failure("Error al obtener los temas del foro"))
        }
    }

    override fun createTopic(topic: ForumTopic): Flow<ApiResponse<Unit>> = flow {
        emit(ApiResponse.Loading)
        try {
            firestore.collection("forumTopics").document(topic.id).set(topic).await()
            emit(ApiResponse.Success(Unit))
        } catch (e: Exception) {
            Log.e(TAG, "createTopic failed", e)
            emit(ApiResponse.Failure("Error al crear el tema"))
        }
    }

    override fun getTopicById(topicId: String): Flow<ApiResponse<ForumTopic>> = flow {
        emit(ApiResponse.Loading)
        try {
            val snapshot = firestore.collection("forumTopics").document(topicId).get().await()
            val topic = snapshot.toObject(ForumTopic::class.java)
            if (topic != null) {
                emit(ApiResponse.Success(topic))
            } else {
                emit(ApiResponse.Failure("Tema no encontrado"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "getTopicById failed", e)
            emit(ApiResponse.Failure("Error al obtener el tema"))
        }
    }

    override fun updateTopic(topic: ForumTopic): Flow<ApiResponse<Unit>> = flow {
        emit(ApiResponse.Loading)
        try {
            firestore.collection("forumTopics").document(topic.id).set(topic).await()
            emit(ApiResponse.Success(Unit))
        } catch (e: Exception) {
            Log.e(TAG, "updateTopic failed", e)
            emit(ApiResponse.Failure("Error al actualizar el tema"))
        }
    }
}
