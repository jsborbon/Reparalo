package com.jsborbon.reparalo.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.CommentRepository
import com.jsborbon.reparalo.data.repository.TutorialRepository
import com.jsborbon.reparalo.data.repository.impl.CommentRepositoryImpl
import com.jsborbon.reparalo.data.repository.impl.TutorialRepositoryImpl
import com.jsborbon.reparalo.models.Author
import com.jsborbon.reparalo.models.Comment
import com.jsborbon.reparalo.models.Material
import com.jsborbon.reparalo.models.Tutorial
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Date

class TutorialDetailViewModel(
    private val tutorialRepository: TutorialRepository = TutorialRepositoryImpl(),
    private val commentRepository: CommentRepository = CommentRepositoryImpl()
) : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    private val _tutorial = MutableStateFlow<ApiResponse<Tutorial>>(ApiResponse.Idle)
    val tutorial: StateFlow<ApiResponse<Tutorial>> = _tutorial

    private val _comments = MutableStateFlow<ApiResponse<List<Comment>>>(ApiResponse.Idle)
    val comments: StateFlow<ApiResponse<List<Comment>>> = _comments

    private val _materials = MutableStateFlow<ApiResponse<List<Material>>>(ApiResponse.Idle)
    val materials: StateFlow<ApiResponse<List<Material>>> = _materials

    private val _userNames = MutableStateFlow<Map<String, String>>(emptyMap())
    val userNames: StateFlow<Map<String, String>> = _userNames

    fun loadTutorial(tutorialId: String) {
        _tutorial.value = ApiResponse.Loading
        viewModelScope.launch {
            try {
                tutorialRepository.getTutorial(tutorialId).collect { response ->
                    _tutorial.value = response
                    if (response is ApiResponse.Success) {
                        loadMaterialsByIds(response.data.materials)
                    }
                }
            } catch (e: Exception) {
                _tutorial.value = ApiResponse.Failure(e.message ?: "Error al cargar el tutorial.")
            }
        }
    }

    private fun loadMaterialsByIds(ids: List<String>) {
        _materials.value = ApiResponse.Loading
        viewModelScope.launch {
            try {
                val materialList: List<Material> = ids.mapNotNull { id ->
                    try {
                        val snapshot = firestore.collection("materials").document(id).get().await()
                        val material: Material? = snapshot.toObject(Material::class.java)
                        material?.apply { this.id = snapshot.id }
                    } catch (e: Exception) {
                        Log.e("TutorialDetailVM", "Failed to load material $id: ${e.message}")
                        null
                    }
                }
                _materials.value = ApiResponse.Success(materialList)
            } catch (_: Exception) {
                _materials.value = ApiResponse.Failure("Error al cargar materiales.")
            }
        }
    }

    fun loadComments(tutorialId: String) {
        _comments.value = ApiResponse.Loading
        viewModelScope.launch {
            try {
                commentRepository.getCommentsByTutorial(tutorialId).collect { response ->
                    _comments.value = response
                    if (response is ApiResponse.Success) {
                        loadUserNames(response.data.map { it.author.uid }.toSet())
                    }
                }
            } catch (e: Exception) {
                _comments.value = ApiResponse.Failure(e.message ?: "Error al cargar los comentarios.")
            }
        }
    }

    private fun loadUserNames(userIds: Set<String>) {
        viewModelScope.launch {
            val namesMap = mutableMapOf<String, String>()
            for (uid in userIds) {
                try {
                    commentRepository.getUserById(uid).collect { response ->
                        if (response is ApiResponse.Success) {
                            namesMap[uid] = response.data.name
                        } else if (response is ApiResponse.Failure) {
                            Log.e("TutorialDetailVM", "Error loading user $uid: ${response.errorMessage}")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("TutorialDetailVM", "Exception loading user $uid: ${e.message}")
                }
            }
            _userNames.value = namesMap
        }
    }

    fun submitComment(tutorialId: String, content: String, rating: Int, userId: String) {
        viewModelScope.launch {
            try {
                commentRepository.createComment(
                    Comment(
                        id = "",
                        tutorialId = tutorialId,
                        author = Author(uid = userId),
                        content = content,
                        rating = rating,
                        date = Date()
                    )
                ).collect { response ->
                    when (response) {
                        is ApiResponse.Success -> loadComments(tutorialId)
                        is ApiResponse.Failure -> Log.e("TutorialDetailVM", "Failed to submit comment: ${response.errorMessage}")
                        ApiResponse.Loading -> Log.i("TutorialDetailVM", "Submitting comment...")
                        ApiResponse.Idle -> Unit
                    }
                }
            } catch (e: Exception) {
                Log.e("TutorialDetailVM", "Exception while submitting comment: ${e.message}")
            }
        }
    }
}
