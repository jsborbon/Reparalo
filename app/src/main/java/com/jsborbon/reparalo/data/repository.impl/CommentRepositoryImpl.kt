package com.jsborbon.reparalo.data.repository.impl

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.CommentRepository
import com.jsborbon.reparalo.models.Author
import com.jsborbon.reparalo.models.Comment
import com.jsborbon.reparalo.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class CommentRepositoryImpl(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
) : CommentRepository {

    private val commentsCollection = firestore.collection("comments")
    private val usersCollection = firestore.collection("users")
    private val TAG = CommentRepositoryImpl::class.java.simpleName

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
            emit(ApiResponse.Failure("Error al obtener los comentarios del tutorial"))
        }
    }.catch { e ->
        Log.e(TAG, "getCommentsByTutorial catch: ${e.message}", e)
        emit(ApiResponse.Failure("Error inesperado al cargar comentarios del tutorial"))
    }

    override fun getCommentsByForumTopic(forumTopicId: String): Flow<ApiResponse<List<Comment>>> = flow {
        emit(ApiResponse.Loading)
        try {
            val snapshot = commentsCollection
                .whereEqualTo("forumTopicId", forumTopicId)
                .get()
                .await()

            val comments = snapshot.documents.mapNotNull { it.toObject(Comment::class.java) }
            emit(ApiResponse.Success(comments))
        } catch (e: Exception) {
            Log.e(TAG, "getCommentsByForumTopic exception: ${e.message}", e)
            emit(ApiResponse.Failure("Error al obtener los comentarios del foro"))
        }
    }.catch { e ->
        Log.e(TAG, "getCommentsByForumTopic catch: ${e.message}", e)
        emit(ApiResponse.Failure("Error inesperado al cargar comentarios del foro"))
    }

    override fun createComment(comment: Comment): Flow<ApiResponse<Comment>> = flow {
        emit(ApiResponse.Loading)

        if (comment.tutorialId.isNullOrBlank() && comment.forumTopicId.isNullOrBlank()) {
            Log.e(TAG, "createComment error: tutorialId and forumTopicId are both null or blank")
            emit(ApiResponse.Failure("El comentario debe estar asociado a un tutorial o a un tema del foro"))
            return@flow
        }

        try {
            commentsCollection.add(comment).await()
            emit(ApiResponse.Success(comment))
        } catch (e: Exception) {
            Log.e(TAG, "createComment exception: ${e.message}", e)
            emit(ApiResponse.Failure("Error al crear el comentario"))
        }
    }.catch { e ->
        Log.e(TAG, "createComment catch: ${e.message}", e)
        emit(ApiResponse.Failure("Error inesperado al guardar el comentario"))
    }

    override fun getUserById(userId: String): Flow<ApiResponse<Author>> = flow {
        emit(ApiResponse.Loading)

        if (userId.isBlank()) {
            Log.e(TAG, "getUserById error: userId is blank or invalid")
            emit(ApiResponse.Failure("ID de usuario inválido"))
            return@flow
        }

        try {
            val snapshot = usersCollection.document(userId).get().await()
            val user = snapshot.toObject(User::class.java)

            if (user != null) {
                val author = Author(name = user.name, uid = user.uid)
                emit(ApiResponse.Success(author))
            } else {
                Log.w(TAG, "getUserById warning: Usuario no encontrado para id=$userId")
                emit(ApiResponse.Failure("Usuario no encontrado"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "getUserById exception: ${e.message}", e)
            emit(ApiResponse.Failure("Error al obtener usuario"))
        }
    }.catch { e ->
        Log.e(TAG, "getUserById catch: ${e.message}", e)
        emit(ApiResponse.Failure("Error inesperado al cargar usuario"))
    }
}
