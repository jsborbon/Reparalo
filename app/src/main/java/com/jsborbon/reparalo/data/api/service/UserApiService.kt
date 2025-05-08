package com.jsborbon.reparalo.data.api.service

import com.jsborbon.reparalo.models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApiService {

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
