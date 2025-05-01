package com.jsborbon.reparalo.data.api

import com.jsborbon.reparalo.models.Comentario
import com.jsborbon.reparalo.models.Tecnico
import com.jsborbon.reparalo.models.Tutorial
import com.jsborbon.reparalo.models.Usuario
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ReparaloApiService {
    // Endpoints de usuarios
    @GET("usuarios/{uid}")
    suspend fun getUsuario(
        @Path("uid") uid: String,
    ): Response<Usuario>

    @PUT("usuarios/{uid}")
    suspend fun actualizarUsuario(
        @Path("uid") uid: String,
        @Body usuario: Usuario,
    ): Response<Usuario>

    // Endpoints de técnicos
    @GET("tecnicos")
    suspend fun getTecnicos(): Response<List<Tecnico>>

    @GET("tecnicos/{uid}")
    suspend fun getTecnico(
        @Path("uid") uid: String,
    ): Response<Tecnico>

    @GET("tecnicos/especialidad/{especialidad}")
    suspend fun getTecnicosPorEspecialidad(
        @Path("especialidad") especialidad: String,
    ): Response<List<Tecnico>>

    // Endpoints de tutoriales
    @GET("tutoriales")
    suspend fun getTutoriales(): Response<List<Tutorial>>

    @GET("tutoriales/{id}")
    suspend fun getTutorial(
        @Path("id") id: String,
    ): Response<Tutorial>

    @GET("tutoriales/categoria/{categoria}")
    suspend fun getTutorialesPorCategoria(
        @Path("categoria") categoria: String,
    ): Response<List<Tutorial>>

    @POST("tutoriales")
    suspend fun crearTutorial(
        @Body tutorial: Tutorial,
    ): Response<Tutorial>

    // Endpoints de comentarios
    @GET("comentarios/tutorial/{idTutorial}")
    suspend fun getComentariosPorTutorial(
        @Path("idTutorial") idTutorial: String,
    ): Response<List<Comentario>>

    @POST("comentarios")
    suspend fun crearComentario(
        @Body comentario: Comentario,
    ): Response<Comentario>
}
