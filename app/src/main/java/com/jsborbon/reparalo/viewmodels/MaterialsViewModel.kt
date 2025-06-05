package com.jsborbon.reparalo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.MaterialRepository
import com.jsborbon.reparalo.data.repository.impl.MaterialRepositoryImpl
import com.jsborbon.reparalo.models.Material
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MaterialsViewModel(
    private val repository: MaterialRepository = MaterialRepositoryImpl(),
) : ViewModel() {

    private val _materials = MutableStateFlow<ApiResponse<List<Material>>>(ApiResponse.Loading)
    val materials: StateFlow<ApiResponse<List<Material>>> = _materials

    private val _materialDetail = MutableStateFlow<ApiResponse<Material>>(ApiResponse.Idle)
    val materialDetail: StateFlow<ApiResponse<Material>> = _materialDetail

    init {
        loadMaterials()
    }

    fun loadMaterials() {
        _materials.value = ApiResponse.Loading
        viewModelScope.launch {
            try {
                repository.getMaterials().collect { response ->
                    _materials.value = response
                }
            } catch (e: Exception) {
                _materials.value = ApiResponse.Failure(e.message ?: "Error al cargar los materiales.")
            }
        }
    }

    fun loadMaterialById(id: String) {
        _materialDetail.value = ApiResponse.Loading
        viewModelScope.launch {
            try {
                repository.getMaterialById(id).collect { response ->
                    _materialDetail.value = response
                }
            } catch (e: Exception) {
                _materialDetail.value = ApiResponse.Failure(e.message ?: "Error al cargar el detalle del material.")
            }
        }
    }

    fun updateMaterial(updated: Material) {
        _materialDetail.value = ApiResponse.Loading
        viewModelScope.launch {
            try {
                repository.updateMaterial(updated).collect { response ->
                    _materialDetail.value = response
                }
            } catch (e: Exception) {
                _materialDetail.value = ApiResponse.Failure(e.message ?: "Error al actualizar el material.")
            }
        }
    }

    fun resetMaterialDetail() {
        _materialDetail.value = ApiResponse.Idle
    }
}
