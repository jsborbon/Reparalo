package com.jsborbon.reparalo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.TechnicianRepository
import com.jsborbon.reparalo.data.repository.impl.TechnicianRepositoryImpl
import com.jsborbon.reparalo.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TechnicianListViewModel(
    private val repository: TechnicianRepository = TechnicianRepositoryImpl()
) : ViewModel() {

    private val _technicians = MutableStateFlow<ApiResponse<List<User>>>(ApiResponse.Loading)
    val technicians: StateFlow<ApiResponse<List<User>>> = _technicians

    private val _selectedSpecialty = MutableStateFlow<String?>(null)
    val selectedSpecialty: StateFlow<String?> = _selectedSpecialty

    fun loadAllTechnicians() {
        viewModelScope.launch {
            repository.getAllTechnicians().collect { response ->
                _technicians.value = response
            }
        }
    }

    fun loadTechniciansBySpecialty(specialty: String, page: Int? = null, pageSize: Int? = null) {
        _selectedSpecialty.value = specialty
        viewModelScope.launch {
            repository.getTechniciansBySpecialty(specialty, page, pageSize).collect {
                _technicians.value = it
            }
        }
    }
}
