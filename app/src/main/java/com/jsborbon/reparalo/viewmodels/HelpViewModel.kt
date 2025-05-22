package com.jsborbon.reparalo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.HelpRepository
import com.jsborbon.reparalo.models.HelpItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HelpViewModel @Inject constructor(
    private val repository: HelpRepository,
) : ViewModel() {

    private val _helpItems = MutableStateFlow<ApiResponse<List<HelpItem>>>(ApiResponse.Loading)
    val helpItems: StateFlow<ApiResponse<List<HelpItem>>> = _helpItems

    init {
        loadHelpItems()
    }

    private fun loadHelpItems() {
        viewModelScope.launch {
            try {
                val response = repository.getHelpItems()
                _helpItems.value = response
            } catch (e: Exception) {
                _helpItems.value = ApiResponse.Failure(e.message ?: "Error desconocido")
            }
        }
    }
}
