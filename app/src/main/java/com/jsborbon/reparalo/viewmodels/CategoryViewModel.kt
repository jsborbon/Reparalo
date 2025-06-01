package com.jsborbon.reparalo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.CategoryRepository
import com.jsborbon.reparalo.data.repository.impl.CategoryRepositoryImpl
import com.jsborbon.reparalo.models.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val categoryRepository: CategoryRepository = CategoryRepositoryImpl(),
) : ViewModel() {

    private val _categories = MutableStateFlow<ApiResponse<List<Category>>>(ApiResponse.Loading)
    val categories: StateFlow<ApiResponse<List<Category>>> = _categories

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            categoryRepository.getCategories().collect {
                _categories.value = it
            }
        }
    }
}
