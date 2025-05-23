package com.jsborbon.reparalo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.TutorialRepository
import com.jsborbon.reparalo.data.repository.impl.TutorialRepositoryImpl
import com.jsborbon.reparalo.models.Tutorial
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TutorialsViewModel(
    private val repository: TutorialRepository = TutorialRepositoryImpl()
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

    private val _favoriteIds = MutableStateFlow<Set<String>>(emptySet())
    val favoriteIds: StateFlow<Set<String>> = _favoriteIds

    private val _isFavorite = MutableStateFlow<ApiResponse<Boolean>>(ApiResponse.Success(false))
    val isFavorite: StateFlow<ApiResponse<Boolean>> = _isFavorite

    private val _favoriteTutorials = MutableStateFlow<ApiResponse<List<Tutorial>>>(ApiResponse.Loading)
    val favoriteTutorials: StateFlow<ApiResponse<List<Tutorial>>> = _favoriteTutorials


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

    fun toggleFavorite(tutorialId: String) {
        _isFavorite.value = ApiResponse.Loading
        viewModelScope.launch {
            val current = _favoriteIds.value
            val isCurrentlyFavorite = current.contains(tutorialId)

            if (isCurrentlyFavorite) {
                repository.removeFavorite(tutorialId).collect {
                    when (it) {
                        is ApiResponse.Success -> {
                            _favoriteIds.value = current - tutorialId
                            _isFavorite.value = ApiResponse.Success(false)
                        }
                        is ApiResponse.Failure -> {
                            _isFavorite.value = ApiResponse.Failure(it.errorMessage)
                        }
                        else -> {}
                    }
                }
            } else {
                repository.addFavorite(tutorialId).collect {
                    when (it) {
                        is ApiResponse.Success -> {
                            _favoriteIds.value = current + tutorialId
                            _isFavorite.value = ApiResponse.Success(true)
                        }
                        is ApiResponse.Failure -> {
                            _isFavorite.value = ApiResponse.Failure(it.errorMessage)
                        }
                        else -> {}
                    }
                }
            }
        }
    }


    fun fetchFavoriteTutorials() {
        viewModelScope.launch {
            repository.getFavoriteTutorials().collectLatest { response ->
                _favoriteTutorials.value = response
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
