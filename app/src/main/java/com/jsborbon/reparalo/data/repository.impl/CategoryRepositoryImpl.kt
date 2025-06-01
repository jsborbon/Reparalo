package com.jsborbon.reparalo.data.repository.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.CategoryRepository
import com.jsborbon.reparalo.models.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class CategoryRepositoryImpl(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
) : CategoryRepository {

    override fun getCategories(): Flow<ApiResponse<List<Category>>> = flow {
        emit(ApiResponse.Loading)
        val snapshot = firestore.collection("categories").get().await()
        val categories = snapshot.documents.mapNotNull { doc ->
            val id = doc.id
            val name = doc.getString("name") ?: return@mapNotNull null
            Category(id = id, name = name)
        }
        emit(ApiResponse.Success(categories))
    }.catch { e ->
        emit(ApiResponse.Failure(e.message ?: "Error cargando categorías"))
    }
}
