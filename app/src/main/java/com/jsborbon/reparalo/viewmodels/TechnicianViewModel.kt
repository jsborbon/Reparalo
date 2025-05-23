package com.jsborbon.reparalo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.TechnicianRepository
import com.jsborbon.reparalo.data.repository.impl.TechnicianRepositoryImpl
import com.jsborbon.reparalo.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TechnicianViewModel(
    private val repository: TechnicianRepository = TechnicianRepositoryImpl()
) : ViewModel() {

    private val _technician = MutableStateFlow<ApiResponse<User>>(ApiResponse.Loading)
    val technician: StateFlow<ApiResponse<User>> = _technician

    fun loadTechnician(uid: String) {
        viewModelScope.launch {
            repository.getTechnicianById(uid).collectLatest { response ->
                _technician.value = response
            }
        }
    }
}
