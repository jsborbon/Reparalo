// HistoryViewModel.kt
package com.jsborbon.reparalo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.HistoryRepository
import com.jsborbon.reparalo.data.repository.impl.HistoryRepositoryImpl
import com.jsborbon.reparalo.models.ServiceHistoryItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val repository: HistoryRepository = HistoryRepositoryImpl(),
) : ViewModel() {

    private val _historyItems =
        MutableStateFlow<ApiResponse<List<ServiceHistoryItem>>>(ApiResponse.Loading)
    val historyItems: StateFlow<ApiResponse<List<ServiceHistoryItem>>> = _historyItems

    private val _serviceDetail =
        MutableStateFlow<ApiResponse<ServiceHistoryItem>>(ApiResponse.Loading)
    val serviceDetail: StateFlow<ApiResponse<ServiceHistoryItem>> = _serviceDetail

    fun fetchServiceHistory(userId: String) {
        _historyItems.value = ApiResponse.Loading
        viewModelScope.launch {
            try {
                repository.getServiceHistory(userId).collect { response ->
                    _historyItems.value = response
                }
            } catch (e: Exception) {
                _historyItems.value =
                    ApiResponse.Failure(e.message ?: "Error al cargar el historial de servicios.")
            }
        }
    }

    fun loadService(id: String) {
        _serviceDetail.value = ApiResponse.Loading
        viewModelScope.launch {
            try {
                repository.fetchServiceById(id).collect { response ->
                    _serviceDetail.value = response
                }
            } catch (e: Exception) {
                _serviceDetail.value =
                    ApiResponse.Failure(e.message ?: "Error al cargar el detalle del servicio.")
            }
        }
    }
}
