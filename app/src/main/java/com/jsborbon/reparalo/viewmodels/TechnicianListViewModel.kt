package com.jsborbon.reparalo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.TechnicianRepository
import com.jsborbon.reparalo.data.repository.impl.TechnicianRepositoryImpl
import com.jsborbon.reparalo.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TechnicianListViewModel(
    private val repository: TechnicianRepository = TechnicianRepositoryImpl(),
) : ViewModel() {

    private val _technicians = MutableStateFlow<ApiResponse<List<User>>>(ApiResponse.Loading)
    val technicians: StateFlow<ApiResponse<List<User>>> = _technicians

    private val _selectedSpecialty = MutableStateFlow<String?>(null)
    val selectedSpecialty: StateFlow<String?> = _selectedSpecialty

    private var allTechnicians: List<User> = emptyList()

    fun loadAllTechnicians() {
        _technicians.value = ApiResponse.Loading
        viewModelScope.launch {
            try {
                repository.getAllTechnicians().collect { response ->
                    if (response is ApiResponse.Success) {
                        allTechnicians = response.data
                        _technicians.value = ApiResponse.Success(allTechnicians)
                        _selectedSpecialty.value = null
                    } else {
                        _technicians.value = response
                    }
                }
            } catch (e: Exception) {
                _technicians.value = ApiResponse.Failure(e.message ?: "Error al cargar los técnicos.")
            }
        }
    }

    fun loadTechniciansBySpecialty(specialty: String) {
        _selectedSpecialty.value = specialty
        _technicians.value = ApiResponse.Loading
        viewModelScope.launch {
            try {
                if (allTechnicians.isEmpty()) {
                    repository.getAllTechnicians().collect { response ->
                        if (response is ApiResponse.Success) {
                            allTechnicians = response.data
                            filterBySpecialty(specialty)
                        } else {
                            _technicians.value = response
                        }
                    }
                } else {
                    filterBySpecialty(specialty)
                }
            } catch (e: Exception) {
                _technicians.value = ApiResponse.Failure(e.message ?: "Error al filtrar por especialidad.")
            }
        }
    }

    private fun filterBySpecialty(specialty: String) {
        val filtered = allTechnicians.filter { it.specialty?.equals(specialty, ignoreCase = true) == true }
        _technicians.value = ApiResponse.Success(filtered)
    }
}
