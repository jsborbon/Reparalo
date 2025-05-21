package com.jsborbon.reparalo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.impl.CommentRepositoryImpl
import com.jsborbon.reparalo.data.repository.impl.TutorialRepositoryImpl
import com.jsborbon.reparalo.data.repository.impl.UserRepositoryImpl
import com.jsborbon.reparalo.models.Comment
import com.jsborbon.reparalo.models.Tutorial
import com.jsborbon.reparalo.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TutorialDetailViewModel @Inject constructor(
    private val tutorialRepository: TutorialRepositoryImpl,
    private val commentRepository: CommentRepositoryImpl,
    private val userRepository: UserRepositoryImpl,
) : ViewModel() {

    private val _tutorial = MutableStateFlow<ApiResponse<Tutorial>>(ApiResponse.Loading)
    val tutorial: StateFlow<ApiResponse<Tutorial>> = _tutorial

    private val _comments = MutableStateFlow<ApiResponse<List<Comment>>>(ApiResponse.Loading)
    val comments: StateFlow<ApiResponse<List<Comment>>> = _comments

    private val _userNames = MutableStateFlow<Map<String, String>>(emptyMap())
    val userNames: StateFlow<Map<String, String>> = _userNames

    fun loadTutorial(tutorialId: String) {
        viewModelScope.launch {
            tutorialRepository.getTutorial(tutorialId).collectLatest { response ->
                _tutorial.value = response
            }
        }
    }

    fun loadComments(tutorialId: String) {
        viewModelScope.launch {
            commentRepository.getCommentsByTutorial(tutorialId).collectLatest { response ->
                _comments.value = response

                if (response is ApiResponse.Success) {
                    val uniqueUserIds = response.data.map { it.userId }.distinct()
                    uniqueUserIds.forEach { userId ->
                        viewModelScope.launch {
                            val user: User? = userRepository.getUserData(userId)
                            user?.let {
                                _userNames.value = _userNames.value + (userId to it.name)
                            }
                        }
                    }
                }
            }
        }
    }

    fun submitComment(tutorialId: String, text: String, rating: Int) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

        val newComment = Comment(
            id = "",
            tutorialId = tutorialId,
            userId = userId,
            content = text,
            date = Date(),
            rating = rating,
        )

        viewModelScope.launch {
            commentRepository.createComment(newComment).collectLatest { response ->
                if (response is ApiResponse.Success) {
                    loadComments(tutorialId)
                }
            }
        }
    }
}
