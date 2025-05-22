package com.jsborbon.reparalo.data.repository.impl

import android.util.Log
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.api.service.TutorialApiService
import com.jsborbon.reparalo.data.repository.TutorialRepository
import com.jsborbon.reparalo.models.Tutorial
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TutorialRepositoryImpl @Inject constructor(
    private val apiService: TutorialApiService,
) : TutorialRepository {

    private val TAG = "TutorialRepositoryImpl"

    override fun getTutorials(): Flow<ApiResponse<List<Tutorial>>> = flow {
        emit(ApiResponse.loading<List<Tutorial>>())
        val response = apiService.getAll()
        if (response.isSuccessful) {
            response.body()?.let {
                emit(ApiResponse.success(it))
            } ?: emit(ApiResponse.failure("Empty response body"))
        } else {
            emit(ApiResponse.failure("Error ${response.code()}: ${response.message()}"))
        }
    }.catch { e ->
        Log.e(TAG, "getTutorials exception: ${e.message}", e)
        emit(ApiResponse.failure("Exception: ${e.localizedMessage}"))
    }

    override fun getTutorialsByCategory(category: String): Flow<ApiResponse<List<Tutorial>>> = flow {
        emit(ApiResponse.loading<List<Tutorial>>())
        val response = apiService.getByCategory(category)
        if (response.isSuccessful) {
            response.body()?.let {
                emit(ApiResponse.success(it))
            } ?: emit(ApiResponse.failure("Empty response body"))
        } else {
            emit(ApiResponse.failure("Error ${response.code()}: ${response.message()}"))
        }
    }.catch { e ->
        Log.e(TAG, "getTutorialsByCategory exception: ${e.message}", e)
        emit(ApiResponse.failure("Exception: ${e.localizedMessage}"))
    }

    override fun getTutorial(id: String): Flow<ApiResponse<Tutorial>> = flow {
        emit(ApiResponse.loading<Tutorial>())
        val response = apiService.getById(id)
        if (response.isSuccessful) {
            response.body()?.let {
                emit(ApiResponse.success(it))
            } ?: emit(ApiResponse.failure("Empty response body"))
        } else {
            emit(ApiResponse.failure("Error ${response.code()}: ${response.message()}"))
        }
    }.catch { e ->
        Log.e(TAG, "getTutorial exception: ${e.message}", e)
        emit(ApiResponse.failure("Exception: ${e.localizedMessage}"))
    }

    override fun createTutorial(tutorial: Tutorial): Flow<ApiResponse<Tutorial>> = flow {
        emit(ApiResponse.loading<Tutorial>())
        val response = apiService.create(tutorial)
        if (response.isSuccessful && response.body() != null) {
            emit(ApiResponse.success(response.body()!!))
        } else {
            emit(ApiResponse.failure("Error ${response.code()}: ${response.message()}"))
        }
    }.catch { e ->
        Log.e(TAG, "createTutorial exception: ${e.message}", e)
        emit(ApiResponse.failure("Exception: ${e.localizedMessage}"))
    }

    override fun updateTutorial(id: String, tutorial: Tutorial): Flow<ApiResponse<Tutorial>> = flow {
        emit(ApiResponse.loading<Tutorial>())
        val response = apiService.update(id, tutorial)
        if (response.isSuccessful && response.body() != null) {
            emit(ApiResponse.success(response.body()!!))
        } else {
            emit(ApiResponse.failure("Error ${response.code()}: ${response.message()}"))
        }
    }.catch { e ->
        Log.e(TAG, "updateTutorial exception: ${e.message}", e)
        emit(ApiResponse.failure("Exception: ${e.localizedMessage}"))
    }

    override fun deleteTutorial(id: String): Flow<ApiResponse<Unit>> = flow {
        emit(ApiResponse.loading<Unit>())
        val response = apiService.delete(id)
        if (response.isSuccessful) {
            emit(ApiResponse.success(Unit))
        } else {
            emit(ApiResponse.failure("Error ${response.code()}: ${response.message()}"))
        }
    }.catch { e ->
        Log.e(TAG, "deleteTutorial exception: ${e.message}", e)
        emit(ApiResponse.failure("Exception: ${e.localizedMessage}"))
    }

    override fun isFavorite(tutorialId: String): Flow<ApiResponse<Boolean>> = flow {
        emit(ApiResponse.loading<Boolean>())
        val response = apiService.isFavorite(tutorialId)
        if (response.isSuccessful) {
            response.body()?.let {
                emit(ApiResponse.success(it))
            } ?: emit(ApiResponse.failure("Empty response body"))
        } else {
            emit(ApiResponse.failure("Error ${response.code()}: ${response.message()}"))
        }
    }.catch { e ->
        Log.e(TAG, "isFavorite exception: ${e.message}", e)
        emit(ApiResponse.failure("Exception: ${e.localizedMessage}"))
    }

    override fun addFavorite(tutorialId: String): Flow<ApiResponse<Unit>> = flow {
        emit(ApiResponse.loading<Unit>())
        val response = apiService.addFavorite(tutorialId)
        if (response.isSuccessful) {
            emit(ApiResponse.success(Unit))
        } else {
            emit(ApiResponse.failure("Error ${response.code()}: ${response.message()}"))
        }
    }.catch { e ->
        Log.e(TAG, "addFavorite exception: ${e.message}", e)
        emit(ApiResponse.failure("Exception: ${e.localizedMessage}"))
    }

    override fun removeFavorite(tutorialId: String): Flow<ApiResponse<Unit>> = flow {
        emit(ApiResponse.loading<Unit>())
        val response = apiService.removeFavorite(tutorialId)
        if (response.isSuccessful) {
            emit(ApiResponse.success(Unit))
        } else {
            emit(ApiResponse.failure("Error ${response.code()}: ${response.message()}"))
        }
    }.catch { e ->
        Log.e(TAG, "removeFavorite exception: ${e.message}", e)
        emit(ApiResponse.failure("Exception: ${e.localizedMessage}"))
    }
}
