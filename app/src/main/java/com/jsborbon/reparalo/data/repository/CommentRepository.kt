package com.jsborbon.reparalo.data.repository

import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.Comment
import kotlinx.coroutines.flow.Flow

/**
 * Repository responsible for managing comments associated with tutorials.
 */
interface CommentRepository {

    /**
     * Retrieves the list of comments for a given tutorial.
     *
     * @param tutorialId Unique identifier of the tutorial.
     * @return A flow emitting loading, success (with list of comments), or failure states.
     */
    fun getCommentsByTutorial(tutorialId: String): Flow<ApiResponse<List<Comment>>>

    /**
     * Creates a new comment for a tutorial.
     *
     * @param comment The [Comment] object to be created.
     * @return A flow emitting loading, success (with the created comment), or failure states.
     */
    fun createComment(comment: Comment): Flow<ApiResponse<Comment>>
}
