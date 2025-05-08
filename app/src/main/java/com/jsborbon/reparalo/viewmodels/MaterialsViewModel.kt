package com.jsborbon.reparalo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.impl.MaterialRepositoryImpl
import com.jsborbon.reparalo.models.Material
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MaterialsViewModel @Inject constructor(
    private val repository: MaterialRepositoryImpl
) : ViewModel() {

    private val _materials = MutableStateFlow<ApiResponse<List<Material>>>(ApiResponse.Loading)
    val materials: StateFlow<ApiResponse<List<Material>>> = _materials

    private val _selectedMaterial = MutableStateFlow<ApiResponse<Material>>(ApiResponse.Loading)
    val selectedMaterial: StateFlow<ApiResponse<Material>> = _selectedMaterial

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
            repository.getMaterialById(id).collectLatest { response ->
                _selectedMaterial.value = response
            }
        }
    }
}
