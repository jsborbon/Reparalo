package com.jsborbon.reparalo.data.api.service

import com.jsborbon.reparalo.models.Material
import retrofit2.http.GET
import retrofit2.http.Path

interface MaterialApiService {

    @GET("materials")
    suspend fun getMaterials(): List<Material>

    @GET("materials/{id}")
    suspend fun getMaterialById(@Path("id") id: String): Material
}
