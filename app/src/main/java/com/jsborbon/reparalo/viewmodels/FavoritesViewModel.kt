package com.jsborbon.reparalo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.impl.FavoritesRepositoryImpl
import com.jsborbon.reparalo.models.Tutorial
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: FavoritesRepositoryImpl,
) : ViewModel() {

    private val _favoriteTutorials = MutableStateFlow<ApiResponse<List<Tutorial>>>(ApiResponse.Loading)
    val favoriteTutorials: StateFlow<ApiResponse<List<Tutorial>>> = _favoriteTutorials.asStateFlow()

    fun fetchFavorites(userId: String) {
        viewModelScope.launch {
            _favoriteTutorials.value = ApiResponse.Loading
            try {
                val result = repository.getFavoriteTutorials(userId)
                _favoriteTutorials.value = ApiResponse.Success(result)
            } catch (e: Exception) {
                _favoriteTutorials.value = ApiResponse.Failure(e.message ?: "Error desconocido")
            }
        }
    }
}
