package com.jsborbon.reparalo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.TutorialRepository
import com.jsborbon.reparalo.models.Tutorial
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TutorialsViewModel @Inject constructor(
    private val repository: TutorialRepository
) : ViewModel() {

    private val _tutorials = MutableStateFlow<ApiResponse<List<Tutorial>>>(ApiResponse.Loading)
    val tutorials: StateFlow<ApiResponse<List<Tutorial>>> = _tutorials

    private val _tutorialsByCategory = MutableStateFlow<ApiResponse<List<Tutorial>>>(ApiResponse.Loading)
    val tutorialsByCategory: StateFlow<ApiResponse<List<Tutorial>>> = _tutorialsByCategory

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory

    private val _createState = MutableStateFlow<ApiResponse<Tutorial>?>(null)
    val createState: StateFlow<ApiResponse<Tutorial>?> = _createState

    private val _updateState = MutableStateFlow<ApiResponse<Tutorial>?>(null)
    val updateState: StateFlow<ApiResponse<Tutorial>?> = _updateState

    private val _deleteState = MutableStateFlow<ApiResponse<Unit>?>(null)
    val deleteState: StateFlow<ApiResponse<Unit>?> = _deleteState

    fun loadTutorials() {
        viewModelScope.launch {
            repository.getTutorials().collectLatest {
                _tutorials.value = it
            }
        }
    }

    fun loadTutorialsByCategory(category: String) {
        viewModelScope.launch {
            repository.getTutorialsByCategory(category).collectLatest {
                _tutorialsByCategory.value = it
            }
        }
    }

    fun selectCategory(category: String?) {
        _selectedCategory.value = category
        category?.let {
            loadTutorialsByCategory(it)
        } ?: loadTutorials()
    }

    fun createTutorial(tutorial: Tutorial) {
        _createState.value = ApiResponse.Loading
        viewModelScope.launch {
            repository.createTutorial(tutorial).collectLatest {
                _createState.value = it
                if (it is ApiResponse.Success) {
                    loadTutorials()
                }
            }
        }
    }

    fun updateTutorial(id: String, tutorial: Tutorial) {
        _updateState.value = ApiResponse.Loading
        viewModelScope.launch {
            repository.updateTutorial(id, tutorial).collectLatest {
                _updateState.value = it
                if (it is ApiResponse.Success) {
                    loadTutorials()
                }
            }
        }
    }

    fun deleteTutorial(id: String) {
        _deleteState.value = ApiResponse.Loading
        viewModelScope.launch {
            repository.deleteTutorial(id).collectLatest {
                _deleteState.value = it
                if (it is ApiResponse.Success) {
                    loadTutorials()
                }
            }
        }
    }

    fun resetCreateState() {
        _createState.value = null
    }

    fun resetUpdateState() {
        _updateState.value = null
    }

    fun resetDeleteState() {
        _deleteState.value = null
    }
}
