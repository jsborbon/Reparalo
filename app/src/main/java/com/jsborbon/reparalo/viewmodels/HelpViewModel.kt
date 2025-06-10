package com.jsborbon.reparalo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.HelpRepository
import com.jsborbon.reparalo.data.repository.impl.HelpRepositoryImpl
import com.jsborbon.reparalo.models.HelpItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HelpViewModel(
    private val repository: HelpRepository = HelpRepositoryImpl(),
) : ViewModel() {

    private val _helpItems = MutableStateFlow<ApiResponse<List<HelpItem>>>(ApiResponse.Loading)
    val helpItems: StateFlow<ApiResponse<List<HelpItem>>> = _helpItems

    init {
        loadHelpItems()
    }

     fun loadHelpItems() {
        _helpItems.value = ApiResponse.Loading
        viewModelScope.launch {
            try {
                repository.getHelpItems().collect { response ->
                    _helpItems.value = response
                }
            } catch (e: Exception) {
                _helpItems.value = ApiResponse.Failure(e.message ?: "Error al cargar la ayuda.")
            }
        }
    }
}
