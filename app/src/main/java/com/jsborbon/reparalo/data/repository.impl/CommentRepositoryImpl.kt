package com.jsborbon.reparalo.data.repository.impl

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.CommentRepository
import com.jsborbon.reparalo.models.Comment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class CommentRepositoryImpl : CommentRepository {

    private val TAG = "CommentRepositoryImpl"
    private val commentsCollection = Firebase.firestore.collection("comments")

    override fun getCommentsByTutorial(tutorialId: String): Flow<ApiResponse<List<Comment>>> = flow {
        emit(ApiResponse.Loading)
        try {
            val snapshot = commentsCollection
                .whereEqualTo("tutorialId", tutorialId)
                .get()
                .await()

            val comments = snapshot.documents.mapNotNull { it.toObject(Comment::class.java) }
            emit(ApiResponse.Success(comments))
        } catch (e: Exception) {
            Log.e(TAG, "getCommentsByTutorial exception: ${e.message}", e)
            emit(ApiResponse.Failure("Error al obtener los comentarios"))
        }
    }.catch { e ->
        Log.e(TAG, "Flow catch: ${e.message}", e)
        emit(ApiResponse.Failure("Error inesperado al cargar comentarios"))
    }

    override fun createComment(comment: Comment): Flow<ApiResponse<Comment>> = flow {
        emit(ApiResponse.Loading)
        try {
            commentsCollection.add(comment).await()
            emit(ApiResponse.Success(comment))
        } catch (e: Exception) {
            Log.e(TAG, "createComment exception: ${e.message}", e)
            emit(ApiResponse.Failure("Error al crear el comentario"))
        }
    }.catch { e ->
        Log.e(TAG, "Flow catch: ${e.message}", e)
        emit(ApiResponse.Failure("Error inesperado al guardar el comentario"))
    }
}
