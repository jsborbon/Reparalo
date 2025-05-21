package com.jsborbon.reparalo.data.api.service

import com.jsborbon.reparalo.models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApiService {

    @GET("usuarios/tecnicos")
    suspend fun getAllTechnicians(): Response<List<User>>

    @GET("usuarios/tecnicos/especialidad")
    suspend fun getTechniciansBySpecialty(
        @Query("especialidad") specialty: String,
        @Query("page") page: Int? = null,
        @Query("pageSize") pageSize: Int? = null
    ): Response<List<User>>

    @GET("usuarios/{uid}")
    suspend fun getUser(
        @Path("uid") uid: String
    ): Response<User>

    @PUT("usuarios/{uid}")
    suspend fun updateUser(
        @Path("uid") uid: String,
        @Body user: User
    ): Response<User>
}
