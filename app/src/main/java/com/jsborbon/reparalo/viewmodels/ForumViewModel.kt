package com.jsborbon.reparalo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.impl.ForumRepositoryImpl
import com.jsborbon.reparalo.models.ForumTopic
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
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

    fun getTopicById(id: String): ForumTopic? {
        val currentState = _topics.value
        return if (currentState is ApiResponse.Success) {
            currentState.data.find { it.id == id }
        } else null
    }

    fun createTopic(title: String, description: String, category: String) {
        _createState.value = ApiResponse.Loading

        val newTopic = ForumTopic(
            id = UUID.randomUUID().toString(),
            title = title,
            description = description,
            category = category,
            author = "anonymous",
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
}
