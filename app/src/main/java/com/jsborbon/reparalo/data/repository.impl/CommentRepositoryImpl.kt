package com.jsborbon.reparalo.data.repository.impl

import android.util.Log
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.api.service.CommentApiService
import com.jsborbon.reparalo.data.repository.CommentRepository
import com.jsborbon.reparalo.models.Comment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val apiService: CommentApiService,
) : CommentRepository {

    private val TAG = "CommentRepositoryImpl"

    override fun getCommentsByTutorial(tutorialId: String): Flow<ApiResponse<List<Comment>>> = flow {
        emit(ApiResponse.Loading)
        val response = apiService.getByTutorial(tutorialId)
        if (response.isSuccessful) {
            response.body()?.let {
                emit(ApiResponse.success(it))
            } ?: emit(ApiResponse.failure("Empty response body"))
        } else {
            emit(ApiResponse.failure("Error ${response.code()}: ${response.message()}"))
        }
    }.catch { e ->
        Log.e(TAG, "getCommentsByTutorial exception: ${e.message}", e)
        emit(ApiResponse.failure("Exception: ${e.localizedMessage}"))
    }

    override fun createComment(comment: Comment): Flow<ApiResponse<Comment>> = flow {
        emit(ApiResponse.Loading)
        val response = apiService.create(comment)
        if (response.isSuccessful) {
            response.body()?.let {
                emit(ApiResponse.success(it))
            } ?: emit(ApiResponse.failure("Empty response body"))
        } else {
            emit(ApiResponse.failure("Error ${response.code()}: ${response.message()}"))
        }
    }.catch { e ->
        Log.e(TAG, "createComment exception: ${e.message}", e)
        emit(ApiResponse.failure("Exception: ${e.localizedMessage}"))
    }
}
