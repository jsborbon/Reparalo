package com.jsborbon.reparalo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsborbon.reparalo.data.ReparaloRepositoryImpl
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.Tutorial
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TutorialesViewModel @Inject constructor(
    private val repository: ReparaloRepositoryImpl,
) : ViewModel() {

    private val _tutoriales = MutableStateFlow<ApiResponse<List<Tutorial>>>(ApiResponse.loading())
    val tutoriales: StateFlow<ApiResponse<List<Tutorial>>> = _tutoriales

    private val _tutorialesPorCategoria = MutableStateFlow<ApiResponse<List<Tutorial>>>(ApiResponse.loading())
    val tutorialesPorCategoria: StateFlow<ApiResponse<List<Tutorial>>> = _tutorialesPorCategoria

    private val _categoriaSeleccionada = MutableStateFlow<String?>(null)
    val categoriaSeleccionada: StateFlow<String?> = _categoriaSeleccionada

    init {
        cargarTutoriales()
    }

    fun cargarTutoriales() {
        viewModelScope.launch {
            repository.getTutoriales().collect { response ->
                _tutoriales.value = response
            }
        }
    }

    fun seleccionarCategoria(categoria: String?) {
        _categoriaSeleccionada.value = categoria
        cargarTutorialesPorCategoria(categoria)
    }

    fun cargarTutorialesPorCategoria(categoria: String) {
        viewModelScope.launch {
            repository.getTutorialesPorCategoria(categoria).collect { response ->
                _tutorialesPorCategoria.value = response
            }
        }
    }
}
