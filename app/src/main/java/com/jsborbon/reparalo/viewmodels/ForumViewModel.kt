package com.jsborbon.reparalo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.CommentRepository
import com.jsborbon.reparalo.data.repository.ForumRepository
import com.jsborbon.reparalo.data.repository.impl.CommentRepositoryImpl
import com.jsborbon.reparalo.data.repository.impl.ForumRepositoryImpl
import com.jsborbon.reparalo.models.Comment
import com.jsborbon.reparalo.models.ForumTopic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class ForumViewModel(
    private val forumRepository: ForumRepository = ForumRepositoryImpl(),
    private val commentRepository: CommentRepository = CommentRepositoryImpl()
) : ViewModel() {

    private val _topics = MutableStateFlow<ApiResponse<List<ForumTopic>>>(ApiResponse.Loading)
    val topics: StateFlow<ApiResponse<List<ForumTopic>>> = _topics

    private val _createState = MutableStateFlow<ApiResponse<Unit>>(ApiResponse.Loading)
    val createState: StateFlow<ApiResponse<Unit>> = _createState

    private val _editState = MutableStateFlow<ApiResponse<Unit>?>(null)
    val editState: StateFlow<ApiResponse<Unit>?> = _editState

    private val _comments = MutableStateFlow<ApiResponse<List<Comment>>>(ApiResponse.Idle)
    val comments: StateFlow<ApiResponse<List<Comment>>> = _comments

    init {
        loadTopics()
    }

    fun loadTopics() {
        _topics.value = ApiResponse.Loading
        viewModelScope.launch {
            try {
                forumRepository.getTopics().collect { response ->
                    _topics.value = response
                }
            } catch (e: Exception) {
                _topics.value = ApiResponse.Failure(e.message ?: "Error al cargar los temas.")
            }
        }
    }

    fun getTopicById(topicId: String): Flow<ApiResponse<ForumTopic>> = flow {
        emit(ApiResponse.Loading)
        when (val current = _topics.value) {
            is ApiResponse.Success -> {
                val match = current.data.find { it.id == topicId }
                if (match != null) {
                    emit(ApiResponse.Success(match))
                } else {
                    emit(ApiResponse.Failure("Tema no encontrado"))
                }
            }
            is ApiResponse.Failure -> emit(ApiResponse.Failure(current.errorMessage))
            else -> emit(ApiResponse.Failure("Datos no disponibles"))
        }
    }

    fun createTopic(topic: ForumTopic) {
        _createState.value = ApiResponse.Loading
        viewModelScope.launch {
            try {
                forumRepository.createTopic(topic).collect { response ->
                    _createState.value = response
                    if (response is ApiResponse.Success) {
                        loadTopics()
                    }
                }
            } catch (e: Exception) {
                _createState.value = ApiResponse.Failure(e.message ?: "Error al crear el tema.")
            }
        }
    }

    fun editTopic(topic: ForumTopic) {
        _editState.value = ApiResponse.Loading
        viewModelScope.launch {
            try {
                forumRepository.updateTopic(topic).collect { response ->
                    _editState.value = response
                    if (response is ApiResponse.Success) {
                        loadTopics()
                    }
                }
            } catch (e: Exception) {
                _editState.value = ApiResponse.Failure(e.message ?: "Error al editar el tema.")
            }
        }
    }

    fun loadCommentsForForumTopic(forumTopicId: String) {
        _comments.value = ApiResponse.Loading
        viewModelScope.launch {
            try {
                commentRepository.getCommentsByForumTopic(forumTopicId).collect { response ->
                    _comments.value = response
                }
            } catch (e: Exception) {
                _comments.value = ApiResponse.Failure("Error al cargar comentarios del foro")
            }
        }
    }

    fun resetEditState() {
        _editState.value = null
    }
}
