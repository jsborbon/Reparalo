package com.jsborbon.reparalo.viewmodels

import android.util.Log
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
import kotlinx.coroutines.launch

class TutorialsViewModel(
    private val repository: TutorialRepository = TutorialRepositoryImpl(),
) : ViewModel() {

    private val _tutorials = MutableStateFlow<ApiResponse<List<Tutorial>>>(ApiResponse.Loading)
    val tutorials: StateFlow<ApiResponse<List<Tutorial>>> = _tutorials

    private val _tutorialsByCategory = MutableStateFlow<ApiResponse<List<Tutorial>>>(ApiResponse.Loading)
    val tutorialsByCategory: StateFlow<ApiResponse<List<Tutorial>>> = _tutorialsByCategory

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory

    private val _createState = MutableStateFlow<ApiResponse<Tutorial>>(ApiResponse.Idle)
    val createState: StateFlow<ApiResponse<Tutorial>> = _createState

    private val _updateState = MutableStateFlow<ApiResponse<Tutorial>>(ApiResponse.Idle)
    val updateState: StateFlow<ApiResponse<Tutorial>> = _updateState

    private val _deleteState = MutableStateFlow<ApiResponse<Unit>>(ApiResponse.Idle)
    val deleteState: StateFlow<ApiResponse<Unit>> = _deleteState

    private val _uiMessage = MutableSharedFlow<String>()
    val uiMessage = _uiMessage.asSharedFlow()

    private val _favoriteIds = MutableStateFlow<Set<String>>(emptySet())
    val favoriteIds: StateFlow<Set<String>> = _favoriteIds

    private val _highlightedTutorial = MutableStateFlow<ApiResponse<Tutorial>>(ApiResponse.Loading)
    val highlightedTutorial: StateFlow<ApiResponse<Tutorial>> = _highlightedTutorial

    init {
        loadHighlightedTutorial()
    }

    fun loadTutorials() {
        _tutorials.value = ApiResponse.Loading
        viewModelScope.launch {
            try {
                repository.getTutorials().collect { response ->
                    _tutorials.value = response
                }
            } catch (e: Exception) {
                _tutorials.value = ApiResponse.Failure(e.message ?: "Error al cargar los tutoriales.")
            }
        }
    }

    fun loadTutorialsByCategory(category: String) {
        _tutorialsByCategory.value = ApiResponse.Loading
        viewModelScope.launch {
            try {
                repository.getTutorialsByCategory(category).collect { response ->
                    _tutorialsByCategory.value = response
                }
            } catch (e: Exception) {
                _tutorialsByCategory.value = ApiResponse.Failure(e.message ?: "Error al cargar por categoría.")
            }
        }
    }

    fun selectCategory(category: String?) {
        if (_selectedCategory.value != category) {
            _selectedCategory.value = category
            if (category != null) {
                loadTutorialsByCategory(category)
            } else {
                loadTutorials()
            }
        }
    }

    fun createTutorial(tutorial: Tutorial) {
        _createState.value = ApiResponse.Loading
        viewModelScope.launch {
            try {
                repository.createTutorial(tutorial).collect { response ->
                    _createState.value = response
                    when (response) {
                        is ApiResponse.Success -> {
                            _uiMessage.emit("Tutorial creado con éxito")
                            loadTutorials()
                        }
                        is ApiResponse.Failure -> {
                            _uiMessage.emit("Error al crear tutorial: ${response.errorMessage}")
                        }
                        ApiResponse.Loading -> {
                            Log.d("TutorialsViewModel", "Creando tutorial...")
                            _uiMessage.emit("Creando tutorial...")
                        }
                        ApiResponse.Idle -> Unit
                    }
                }
            } catch (e: Exception) {
                _createState.value = ApiResponse.Failure(e.message ?: "Error inesperado al crear tutorial.")
            }
        }
    }

    fun updateTutorial(id: String, tutorial: Tutorial) {
        _updateState.value = ApiResponse.Loading
        viewModelScope.launch {
            try {
                repository.updateTutorial(id, tutorial).collect { response ->
                    _updateState.value = response
                    when (response) {
                        is ApiResponse.Success -> {
                            _uiMessage.emit("Tutorial actualizado con éxito")
                            loadTutorials()
                        }
                        is ApiResponse.Failure -> {
                            _uiMessage.emit("Error al actualizar tutorial: ${response.errorMessage}")
                        }
                        ApiResponse.Loading -> {
                            Log.d("TutorialsViewModel", "Actualizando tutorial...")
                            _uiMessage.emit("Actualizando tutorial...")
                        }
                        ApiResponse.Idle -> Unit
                    }
                }
            } catch (e: Exception) {
                _updateState.value = ApiResponse.Failure(e.message ?: "Error inesperado al actualizar.")
            }
        }
    }

    fun deleteTutorial(id: String) {
        _deleteState.value = ApiResponse.Loading
        viewModelScope.launch {
            try {
                repository.deleteTutorial(id).collect { response ->
                    _deleteState.value = response
                    when (response) {
                        is ApiResponse.Success -> {
                            _uiMessage.emit("Tutorial eliminado con éxito")
                            loadTutorials()
                        }
                        is ApiResponse.Failure -> {
                            _uiMessage.emit("Error al eliminar tutorial: ${response.errorMessage}")
                        }
                        ApiResponse.Loading -> {
                            Log.d("TutorialsViewModel", "Eliminando tutorial...")
                            _uiMessage.emit("Eliminando tutorial...")
                        }
                        ApiResponse.Idle -> Unit
                    }
                }
            } catch (e: Exception) {
                _deleteState.value = ApiResponse.Failure(e.message ?: "Error inesperado al eliminar.")
            }
        }
    }

    fun loadFavorites() {
        viewModelScope.launch {
            try {
                repository.getFavoriteTutorialIds().collect { response ->
                    when (response) {
                        is ApiResponse.Success -> {
                            _favoriteIds.value = response.data.toSet()
                        }
                        is ApiResponse.Failure -> {
                            _uiMessage.emit("Error al cargar favoritos: ${response.errorMessage}")
                        }
                        ApiResponse.Loading -> {
                            Log.d("TutorialsViewModel", "Cargando favoritos...")
                            _uiMessage.emit("Cargando favoritos...")
                        }
                        ApiResponse.Idle -> Unit
                    }
                }
            } catch (e: Exception) {
                _uiMessage.emit("Error inesperado al cargar favoritos: ${e.message}")
            }
        }
    }

    fun loadHighlightedTutorial() {
        _highlightedTutorial.value = ApiResponse.Loading
        viewModelScope.launch {
            try {
                repository.getHighlightedTutorial().collect { response ->
                    _highlightedTutorial.value = response
                }
            } catch (e: Exception) {
                _highlightedTutorial.value = ApiResponse.Failure(e.message ?: "Error al cargar tutorial destacado.")
            }
        }
    }

    fun toggleFavorite(tutorialId: String) {
        viewModelScope.launch {
            try {
                repository.toggleFavorite(tutorialId).collect { response ->
                    when (response) {
                        is ApiResponse.Success -> {
                            val updatedFavorites = _favoriteIds.value.toMutableSet()
                            if (updatedFavorites.contains(tutorialId)) {
                                updatedFavorites.remove(tutorialId)
                            } else {
                                updatedFavorites.add(tutorialId)
                            }
                            _favoriteIds.value = updatedFavorites
                        }
                        is ApiResponse.Failure -> {
                            _uiMessage.emit("Error al actualizar favorito: ${response.errorMessage}")
                        }
                        ApiResponse.Loading -> {
                            Log.d("TutorialsViewModel", "Actualizando favoritos...")
                            _uiMessage.emit("Actualizando favoritos...")
                        }
                        ApiResponse.Idle -> Unit
                    }
                }
            } catch (e: Exception) {
                _uiMessage.emit("Error inesperado al actualizar favorito: ${e.message}")
            }
        }
    }

    fun resetCreateState() {
        _createState.value = ApiResponse.Idle
    }

    fun resetUpdateState() {
        _updateState.value = ApiResponse.Idle
    }

    fun resetDeleteState() {
        _deleteState.value = ApiResponse.Idle
    }
}
