package com.jsborbon.reparalo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsborbon.reparalo.data.ReparaloRepositoryImpl
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.Tecnico
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TecnicosViewModel @Inject constructor(
    private val repository: ReparaloRepositoryImpl,
) : ViewModel() {

    private val _tecnicos = MutableStateFlow<ApiResponse<List<Tecnico>>>(ApiResponse.loading())
    val tecnicos: StateFlow<ApiResponse<List<Tecnico>>> = _tecnicos

    private val _tecnicosPorEspecialidad = MutableStateFlow<ApiResponse<List<Tecnico>>>(ApiResponse.loading())
    val tecnicosPorEspecialidad: StateFlow<ApiResponse<List<Tecnico>>> = _tecnicosPorEspecialidad

    private val _especialidadSeleccionada = MutableStateFlow<String?>(null)
    val especialidadSeleccionada: StateFlow<String?> = _especialidadSeleccionada

    init {
        cargarTecnicos()
    }

    fun cargarTecnicos() {
        viewModelScope.launch {
            repository.getTecnicos().collect { response ->
                _tecnicos.value = response
            }
        }
    }

    fun seleccionarEspecialidad(especialidad: String) {
        _especialidadSeleccionada.value = especialidad
        cargarTecnicosPorEspecialidad(especialidad)
    }

    fun cargarTecnicosPorEspecialidad(especialidad: String) {
        viewModelScope.launch {
            repository.getTecnicosPorEspecialidad(especialidad).collect { response ->
                _tecnicosPorEspecialidad.value = response
            }
        }
    }
}
