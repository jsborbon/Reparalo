package com.jsborbon.reparalo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.NotificationRepository
import com.jsborbon.reparalo.data.repository.impl.NotificationRepositoryImpl
import com.jsborbon.reparalo.models.NotificationItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val repository: NotificationRepository = NotificationRepositoryImpl()
) : ViewModel() {

    private val _notifications = MutableStateFlow<ApiResponse<List<NotificationItem>>>(ApiResponse.Loading)
    val notifications: StateFlow<ApiResponse<List<NotificationItem>>> = _notifications

    init {
        loadNotifications()
    }

    fun loadNotifications() {
        viewModelScope.launch {
            repository.getNotifications().collectLatest {
                _notifications.value = it
            }
        }
    }
}
