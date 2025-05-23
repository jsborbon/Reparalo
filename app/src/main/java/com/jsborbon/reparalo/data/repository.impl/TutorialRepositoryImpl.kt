package com.jsborbon.reparalo.data.repository.impl

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.TutorialRepository
import com.jsborbon.reparalo.models.Tutorial
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class TutorialRepositoryImpl : TutorialRepository {

    private val TAG = "TutorialRepositoryImpl"
    private val db = Firebase.firestore
    private val userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

    override fun getTutorials(): Flow<ApiResponse<List<Tutorial>>> = flow {
        emit(ApiResponse.Loading)
        val snapshot = db.collection("tutorials").get().await()
        val tutorials = snapshot.documents.mapNotNull { it.toObject(Tutorial::class.java) }
        emit(ApiResponse.Success(tutorials))
    }.catch { e ->
        Log.e(TAG, "getTutorials exception: ${e.message}", e)
        emit(ApiResponse.Failure("Error al cargar los tutoriales"))
    }

    override fun getTutorialsByCategory(category: String): Flow<ApiResponse<List<Tutorial>>> = flow {
        emit(ApiResponse.Loading)
        val snapshot = db.collection("tutorials")
            .whereEqualTo("category", category)
            .get()
            .await()
        val tutorials = snapshot.documents.mapNotNull { it.toObject(Tutorial::class.java) }
        emit(ApiResponse.Success(tutorials))
    }.catch { e ->
        Log.e(TAG, "getTutorialsByCategory exception: ${e.message}", e)
        emit(ApiResponse.Failure("Error al cargar tutoriales por categoría"))
    }

    override fun getTutorial(id: String): Flow<ApiResponse<Tutorial>> = flow {
        emit(ApiResponse.Loading)
        val snapshot = db.collection("tutorials").document(id).get().await()
        val tutorial = snapshot.toObject(Tutorial::class.java)
        if (tutorial != null) {
            emit(ApiResponse.Success(tutorial))
        } else {
            emit(ApiResponse.Failure("Tutorial no encontrado"))
        }
    }.catch { e ->
        Log.e(TAG, "getTutorial exception: ${e.message}", e)
        emit(ApiResponse.Failure("Error al cargar el tutorial"))
    }

    override fun createTutorial(tutorial: Tutorial): Flow<ApiResponse<Tutorial>> = flow {
        emit(ApiResponse.Loading)
        val docRef = db.collection("tutorials").add(tutorial).await()
        val created = docRef.get().await().toObject(Tutorial::class.java)
        if (created != null) {
            emit(ApiResponse.Success(created))
        } else {
            emit(ApiResponse.Failure("Error al crear el tutorial"))
        }
    }.catch { e ->
        Log.e(TAG, "createTutorial exception: ${e.message}", e)
        emit(ApiResponse.Failure("Error al crear el tutorial"))
    }

    override fun updateTutorial(id: String, tutorial: Tutorial): Flow<ApiResponse<Tutorial>> = flow {
        emit(ApiResponse.Loading)
        db.collection("tutorials").document(id).set(tutorial).await()
        emit(ApiResponse.Success(tutorial))
    }.catch { e ->
        Log.e(TAG, "updateTutorial exception: ${e.message}", e)
        emit(ApiResponse.Failure("Error al actualizar el tutorial"))
    }

    override fun deleteTutorial(id: String): Flow<ApiResponse<Unit>> = flow {
        emit(ApiResponse.Loading)
        db.collection("tutorials").document(id).delete().await()
        emit(ApiResponse.Success(Unit))
    }.catch { e ->
        Log.e(TAG, "deleteTutorial exception: ${e.message}", e)
        emit(ApiResponse.Failure("Error al eliminar el tutorial"))
    }

    override fun getFavoriteTutorials(): Flow<ApiResponse<List<Tutorial>>> = flow {
        emit(ApiResponse.Loading)
        val favoritesSnapshot = db.collection("users").document(userId)
            .collection("favorites")
            .get()
            .await()

        val favoriteIds = favoritesSnapshot.documents.map { it.id }

        if (favoriteIds.isEmpty()) {
            emit(ApiResponse.Success(emptyList()))
            return@flow
        }

        val tutorialsSnapshot = db.collection("tutorials")
            .whereIn("id", favoriteIds)
            .get()
            .await()

        val favoriteTutorials = tutorialsSnapshot.documents.mapNotNull { it.toObject(Tutorial::class.java) }
        emit(ApiResponse.Success(favoriteTutorials))
    }.catch { e ->
        Log.e(TAG, "getFavoriteTutorials exception: ${e.message}", e)
        emit(ApiResponse.Failure("Error al cargar favoritos"))
    }

    override fun isFavorite(tutorialId: String): Flow<ApiResponse<Boolean>> = flow {
        emit(ApiResponse.Loading)
        val doc = db.collection("users").document(userId)
            .collection("favorites").document(tutorialId).get().await()
        emit(ApiResponse.Success(doc.exists()))
    }.catch { e ->
        Log.e(TAG, "isFavorite exception: ${e.message}", e)
        emit(ApiResponse.Failure("Error al verificar favorito"))
    }

    override fun addFavorite(tutorialId: String): Flow<ApiResponse<Unit>> = flow {
        emit(ApiResponse.Loading)
        db.collection("users").document(userId)
            .collection("favorites").document(tutorialId)
            .set(mapOf("timestamp" to System.currentTimeMillis())).await()
        emit(ApiResponse.Success(Unit))
    }.catch { e ->
        Log.e(TAG, "addFavorite exception: ${e.message}", e)
        emit(ApiResponse.Failure("Error al añadir a favoritos"))
    }

    override fun removeFavorite(tutorialId: String): Flow<ApiResponse<Unit>> = flow {
        emit(ApiResponse.Loading)
        db.collection("users").document(userId)
            .collection("favorites").document(tutorialId).delete().await()
        emit(ApiResponse.Success(Unit))
    }.catch { e ->
        Log.e(TAG, "removeFavorite exception: ${e.message}", e)
        emit(ApiResponse.Failure("Error al eliminar de favoritos"))
    }
}
