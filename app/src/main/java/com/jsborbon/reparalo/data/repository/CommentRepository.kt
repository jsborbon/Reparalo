package com.jsborbon.reparalo.data.repository

import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.Comment
import kotlinx.coroutines.flow.Flow

interface CommentRepository {

    fun getCommentsByTutorial(tutorialId: String): Flow<ApiResponse<List<Comment>>>
    fun createComment(comment: Comment): Flow<ApiResponse<Comment>>
}
