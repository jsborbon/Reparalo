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

/**
 * Implementation of [TutorialRepository] that manages tutorial-related operations via the API.
 */
class TutorialRepositoryImpl @Inject constructor(
    private val apiService: TutorialApiService
) : TutorialRepository {

    private val TAG = "TutorialRepositoryImpl"

    override fun getTutorials(): Flow<ApiResponse<List<Tutorial>>> = flow {
        emit(ApiResponse.Loading)
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
        emit(ApiResponse.Loading)
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
        emit(ApiResponse.Loading)
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
        emit(ApiResponse.Loading)
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
        emit(ApiResponse.Loading)
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
        emit(ApiResponse.Loading)
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
}
