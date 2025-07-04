package com.jsborbon.reparalo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.NotificationRepository
import com.jsborbon.reparalo.data.repository.impl.NotificationRepositoryImpl
import com.jsborbon.reparalo.models.NotificationItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val repository: NotificationRepository = NotificationRepositoryImpl(),
) : ViewModel() {

    private val _notifications = MutableStateFlow<ApiResponse<List<NotificationItem>>>(ApiResponse.Loading)
    val notifications: StateFlow<ApiResponse<List<NotificationItem>>> = _notifications

    init {
        loadNotifications()
    }

    fun loadNotifications() {
        _notifications.value = ApiResponse.Loading
        viewModelScope.launch {
            try {
                repository.getNotifications().collect { response ->
                    _notifications.value = response
                }
            } catch (e: Exception) {
                _notifications.value = ApiResponse.Failure(e.message ?: "Error al cargar las notificaciones.")
            }
        }
    }

    fun markAsRead(notificationId: String) {
        val currentState = _notifications.value
        if (currentState is ApiResponse.Success) {
            val updated = currentState.data.map {
                if (it.id == notificationId) it.copy(isRead = true) else it
            }
            _notifications.value = ApiResponse.Success(updated)
        }
    }

    fun dismissNotification(notificationId: String) {
        val currentState = _notifications.value
        if (currentState is ApiResponse.Success) {
            val updated = currentState.data.filterNot { it.id == notificationId }
            _notifications.value = ApiResponse.Success(updated)
        }
    }
}
