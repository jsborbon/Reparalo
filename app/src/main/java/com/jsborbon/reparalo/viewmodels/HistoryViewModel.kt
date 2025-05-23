package com.jsborbon.reparalo.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.HistoryRepository
import com.jsborbon.reparalo.data.repository.impl.HistoryRepositoryImpl
import com.jsborbon.reparalo.models.ServiceHistoryItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val repository: HistoryRepository = HistoryRepositoryImpl()
) : ViewModel() {

    private val _historyItems = MutableStateFlow<ApiResponse<List<ServiceHistoryItem>>>(ApiResponse.Loading)
    val historyItems: StateFlow<ApiResponse<List<ServiceHistoryItem>>> = _historyItems.asStateFlow()

    fun fetchServiceHistory() {
        viewModelScope.launch {
            _historyItems.value = ApiResponse.Loading
            try {
                val result = repository.getServiceHistory()
                _historyItems.value = ApiResponse.Success(result)
            } catch (e: Exception) {
                _historyItems.value = ApiResponse.Failure("Error al cargar el historial de servicios")
            }
        }
    }

    private val _serviceDetail = mutableStateOf<ApiResponse<ServiceHistoryItem>>(ApiResponse.Loading)
    val serviceDetail: State<ApiResponse<ServiceHistoryItem>> = _serviceDetail

    fun loadService(id: String) {
        viewModelScope.launch {
            _serviceDetail.value = ApiResponse.Loading
            _serviceDetail.value = repository.fetchServiceById(id)
        }
    }
}
