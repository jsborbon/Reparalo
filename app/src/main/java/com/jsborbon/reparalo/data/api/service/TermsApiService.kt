package com.jsborbon.reparalo.data.api.service

import com.jsborbon.reparalo.models.TermsAndConditions
import retrofit2.Response
import retrofit2.http.GET

interface TermsApiService {

    @GET("api/terms")
    suspend fun fetchTerms(): Response<TermsAndConditions>

}
