package com.jsborbon.reparalo.data.api.service

import com.jsborbon.reparalo.models.Material
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface MaterialApiService {

    @GET("materials")
    suspend fun getMaterials(): List<Material>

    @GET("materials/{id}")
    suspend fun getMaterialById(@Path("id") id: String): Material

    @PUT("materials/{id}")
    suspend fun updateMaterial(@Path("id") id: String, @Body material: Material): Material
}
