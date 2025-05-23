package com.jsborbon.reparalo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.MaterialRepository
import com.jsborbon.reparalo.data.repository.impl.MaterialRepositoryImpl
import com.jsborbon.reparalo.models.Material
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MaterialsViewModel(
    private val repository: MaterialRepository = MaterialRepositoryImpl()
) : ViewModel() {

    private val _materials = MutableStateFlow<ApiResponse<List<Material>>>(ApiResponse.Loading)
    val materials: StateFlow<ApiResponse<List<Material>>> = _materials

    private val _materialDetail = MutableStateFlow<ApiResponse<Material>?>(null)
    val materialDetail: StateFlow<ApiResponse<Material>?> = _materialDetail

    init {
        loadMaterials()
    }

    fun loadMaterials() {
        viewModelScope.launch {
            repository.getMaterials().collectLatest { response ->
                _materials.value = response
            }
        }
    }

    fun loadMaterialById(id: String) {
        viewModelScope.launch {
            _materialDetail.value = ApiResponse.Loading
            _materialDetail.value = repository.getMaterialById(id)
        }
    }

    fun updateMaterial(updated: Material) {
        viewModelScope.launch {
            _materialDetail.value = ApiResponse.Loading
            repository.updateMaterial(updated).collectLatest { response ->
                _materialDetail.value = response
            }
        }
    }
}
