package com.jsborbon.reparalo.data.api.service

import com.jsborbon.reparalo.models.NotificationItem
import retrofit2.Response
import retrofit2.http.GET

interface NotificationApiService {
    @GET("notificaciones")
    suspend fun getNotifications(): Response<List<NotificationItem>>
}
