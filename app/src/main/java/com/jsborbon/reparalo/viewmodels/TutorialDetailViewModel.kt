package com.jsborbon.reparalo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsborbon.reparalo.data.ReparaloRepositoryImpl
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.models.Comentario
import com.jsborbon.reparalo.models.Tutorial
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TutorialDetailViewModel @Inject constructor(
    private val repository: ReparaloRepositoryImpl,
) : ViewModel() {

    private val _tutorial = MutableStateFlow<ApiResponse<Tutorial>>(ApiResponse.loading())
    val tutorial: StateFlow<ApiResponse<Tutorial>> = _tutorial

    private val _comentarios = MutableStateFlow<ApiResponse<List<Comentario>>>(ApiResponse.loading())
    val comentarios: StateFlow<ApiResponse<List<Comentario>>> = _comentarios

    private val _nombresUsuarios = MutableStateFlow<Map<String, String>>(emptyMap())
    val nombresUsuarios: StateFlow<Map<String, String>> = _nombresUsuarios

    fun cargarTutorial(tutorialId: String) {
        viewModelScope.launch {
            repository.getTutorial(tutorialId).collectLatest { response ->
                _tutorial.value = response
            }
        }
    }

    fun cargarComentarios(tutorialId: String) {
        viewModelScope.launch {
            repository.getComentariosPorTutorial(tutorialId).collectLatest { response ->
                _comentarios.value = response
                if (response is ApiResponse.Success) {
                    response.data.map { it.uidUsuario }
                        .distinct()
                        .forEach { uid ->
                            viewModelScope.launch {
                                val usuario = repository.getUsuarioData(uid)
                                if (usuario != null) {
                                    _nombresUsuarios.value =
                                        _nombresUsuarios.value + (uid to usuario.nombre)
                                }
                            }
                        }
                }
            }
        }
    }

    fun enviarComentario(
        tutorialId: String,
        texto: String,
        calificacion: Int,
    ) {
        val userId = "usuario_actual" // TODO: reemplazar con ID real del usuario autenticado

        val nuevoComentario = Comentario(
            id = "",
            idTutorial = tutorialId,
            uidUsuario = userId,
            contenido = texto,
            fecha = Date(),
            calificacion = calificacion,
        )

        viewModelScope.launch {
            repository.crearComentario(nuevoComentario).collectLatest { response ->
                if (response is ApiResponse.Success) {
                    cargarComentarios(tutorialId)
                }
            }
        }
    }
}
