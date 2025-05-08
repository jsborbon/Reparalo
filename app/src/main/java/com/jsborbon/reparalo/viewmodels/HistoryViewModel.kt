package com.jsborbon.reparalo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.impl.HistoryRepositoryImpl
import com.jsborbon.reparalo.models.ServiceHistoryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: HistoryRepositoryImpl
) : ViewModel() {

    private val _historyItems = MutableStateFlow<ApiResponse<List<ServiceHistoryItem>>>(ApiResponse.Loading)
    val historyItems: StateFlow<ApiResponse<List<ServiceHistoryItem>>> = _historyItems.asStateFlow()

    fun fetchServiceHistory(userId: String) {
        viewModelScope.launch {
            _historyItems.value = ApiResponse.Loading
            try {
                val result = repository.getServiceHistory(userId)
                _historyItems.value = ApiResponse.Success(result)
            } catch (e: Exception) {
                _historyItems.value = ApiResponse.Failure(e.message ?: "Error desconocido")
            }
        }
    }
}
