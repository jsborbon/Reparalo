package com.jsborbon.reparalo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.TermsRepository
import com.jsborbon.reparalo.data.repository.impl.TermsRepositoryImpl
import com.jsborbon.reparalo.models.TermsAndConditions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TermsViewModel(
    private val repository: TermsRepository = TermsRepositoryImpl()
) : ViewModel() {

    private val _terms = MutableStateFlow<ApiResponse<TermsAndConditions>>(ApiResponse.Loading)
    val terms: StateFlow<ApiResponse<TermsAndConditions>> = _terms

    init {
        loadTerms()
    }

    fun loadTerms() {
        viewModelScope.launch {
            _terms.value = ApiResponse.Loading
            val result = repository.getTermsAndConditions()
            _terms.value = result
        }
    }
}
