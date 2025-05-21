package com.jsborbon.reparalo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.impl.TutorialRepositoryImpl
import com.jsborbon.reparalo.models.Tutorial
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TutorialsViewModel @Inject constructor(
    private val repository: TutorialRepositoryImpl,
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

    private val _uiMessage = MutableSharedFlow<String>()
    val uiMessage = _uiMessage.asSharedFlow()

    private val _isFavorite = MutableStateFlow<ApiResponse<Boolean>>(ApiResponse.Loading)
    val isFavorite: StateFlow<ApiResponse<Boolean>> = _isFavorite

    fun loadTutorials() {
        viewModelScope.launch {
            repository.getTutorials().collect {
                _tutorials.value = it
            }
        }
    }

    fun loadTutorialsByCategory(category: String) {
        viewModelScope.launch {
            repository.getTutorialsByCategory(category).collect {
                _tutorialsByCategory.value = it
            }
        }
    }

    fun selectCategory(category: String?) {
        _selectedCategory.value = category
        if (category != null) {
            loadTutorialsByCategory(category)
        } else {
            loadTutorials()
        }
    }

    fun createTutorial(tutorial: Tutorial) {
        _createState.value = ApiResponse.Loading
        viewModelScope.launch {
            repository.createTutorial(tutorial).collect {
                _createState.value = it
                when (it) {
                    is ApiResponse.Success -> {
                        _uiMessage.emit("Tutorial creado con éxito")
                        loadTutorials()
                    }
                    is ApiResponse.Failure -> {
                        _uiMessage.emit("Error al crear tutorial: ${it.errorMessage}")
                    }
                    else -> {}
                }
            }
        }
    }

    fun updateTutorial(id: String, tutorial: Tutorial) {
        _updateState.value = ApiResponse.Loading
        viewModelScope.launch {
            repository.updateTutorial(id, tutorial).collect {
                _updateState.value = it
                when (it) {
                    is ApiResponse.Success -> {
                        _uiMessage.emit("Tutorial actualizado con éxito")
                        loadTutorials()
                    }
                    is ApiResponse.Failure -> {
                        _uiMessage.emit("Error al actualizar tutorial: ${it.errorMessage}")
                    }
                    else -> {}
                }
            }
        }
    }

    fun deleteTutorial(id: String) {
        _deleteState.value = ApiResponse.Loading
        viewModelScope.launch {
            repository.deleteTutorial(id).collect {
                _deleteState.value = it
                when (it) {
                    is ApiResponse.Success -> {
                        _uiMessage.emit("Tutorial eliminado con éxito")
                        loadTutorials()
                    }
                    is ApiResponse.Failure -> {
                        _uiMessage.emit("Error al eliminar tutorial: ${it.errorMessage}")
                    }
                    else -> {}
                }
            }
        }
    }

    fun loadIsFavorite(tutorialId: String) {
        viewModelScope.launch {
            repository.isFavorite(tutorialId).collect {
                _isFavorite.value = it
            }
        }
    }

    fun toggleFavorite(tutorialId: String) {
        viewModelScope.launch {
            when (val current = _isFavorite.value) {
                is ApiResponse.Success -> {
                    val currentlyFavorite = current.data
                    if (currentlyFavorite) {
                        repository.removeFavorite(tutorialId).collect {
                            if (it is ApiResponse.Success) {
                                _isFavorite.value = ApiResponse.Success(false)
                            }
                        }
                    } else {
                        repository.addFavorite(tutorialId).collect {
                            if (it is ApiResponse.Success) {
                                _isFavorite.value = ApiResponse.Success(true)
                            }
                        }
                    }
                }
                else -> {
                    loadIsFavorite(tutorialId)
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
