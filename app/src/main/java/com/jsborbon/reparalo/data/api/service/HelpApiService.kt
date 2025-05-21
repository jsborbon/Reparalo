package com.jsborbon.reparalo.data.api.service

import com.jsborbon.reparalo.models.HelpItem
import retrofit2.Response
import retrofit2.http.GET

interface HelpApiService {

    @GET("api/help-items")
    suspend fun fetchHelpItems(): Response<List<HelpItem>>
}
