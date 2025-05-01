package com.jsborbon.reparalo.data

import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.Comentario
import com.jsborbon.reparalo.models.Tecnico
import com.jsborbon.reparalo.models.Tutorial
import kotlinx.coroutines.flow.Flow

interface DataRepository {
    fun getTutoriales(): Flow<ApiResponse<List<Tutorial>>>
    fun getTutorialesPorCategoria(categoria: String): Flow<ApiResponse<List<Tutorial>>>
    fun getTecnicos(): Flow<ApiResponse<List<Tecnico>>>
    fun getTecnicosPorEspecialidad(especialidad: String): Flow<ApiResponse<List<Tecnico>>>
    fun getComentariosPorTutorial(idTutorial: String): Flow<ApiResponse<List<Comentario>>>
    fun crearComentario(comentario: Comentario): Flow<ApiResponse<Comentario>>
    fun getTutorial(string: String)
    fun getUsuarioData(string: kotlin.String)
}
