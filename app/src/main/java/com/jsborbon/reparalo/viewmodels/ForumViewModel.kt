package com.jsborbon.reparalo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.impl.ForumRepositoryImpl
import com.jsborbon.reparalo.models.ForumTopic
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ForumViewModel @Inject constructor(
    private val repository: ForumRepositoryImpl
) : ViewModel() {

    private val _topics = MutableStateFlow<ApiResponse<List<ForumTopic>>>(ApiResponse.Loading)
    val topics: StateFlow<ApiResponse<List<ForumTopic>>> = _topics

    private val _createState = MutableStateFlow<ApiResponse<Boolean>>(ApiResponse.Loading)
    val createState: StateFlow<ApiResponse<Boolean>> = _createState

    private val _editState = MutableStateFlow<ApiResponse<Boolean>?>(null)
    val editState: StateFlow<ApiResponse<Boolean>?> = _editState

    init {
        loadTopics()
    }

    fun loadTopics() {
        _topics.value = ApiResponse.Loading
        viewModelScope.launch {
            repository.getTopics().collectLatest { response ->
                _topics.value = response
            }
        }
    }

    fun getTopicById(topicId: String): Flow<ApiResponse<ForumTopic>> = flow {
        emit(ApiResponse.Loading)
        val current = _topics.value
        if (current is ApiResponse.Success) {
            val match = current.data.find { it.id == topicId }
            if (match != null) {
                emit(ApiResponse.Success(match))
            } else {
                emit(ApiResponse.Failure("Tema no encontrado"))
            }
        } else if (current is ApiResponse.Failure) {
            emit(ApiResponse.Failure(current.errorMessage))
        }
    }

    fun createTopic(title: String, description: String, category: String) {
        _createState.value = ApiResponse.Loading

        val newTopic = ForumTopic(
            id = UUID.randomUUID().toString(),
            title = title,
            description = description,
            category = category,
            author = "Anónimo",
            date = Date(),
            preview = description.take(100),
            comments = 0,
            likes = 0,
            views = 0
        )

        viewModelScope.launch {
            repository.createTopic(newTopic).collectLatest { response ->
                _createState.value = response
                if (response is ApiResponse.Success) {
                    loadTopics()
                }
            }
        }
    }

    fun editTopic(id: String, title: String, description: String, category: String) {
        _editState.value = ApiResponse.Loading
        viewModelScope.launch {
            repository.updateTopic(id, title, description, category).collectLatest { response ->
                _editState.value = response
                if (response is ApiResponse.Success) {
                    loadTopics()
                }
            }
        }
    }

    fun resetEditState() {
        _editState.value = null
    }
}
