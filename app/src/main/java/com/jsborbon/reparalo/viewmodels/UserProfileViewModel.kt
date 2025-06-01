package com.jsborbon.reparalo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.UserRepository
import com.jsborbon.reparalo.data.repository.impl.UserRepositoryImpl
import com.jsborbon.reparalo.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UserProfileViewModel(
    private val repository: UserRepository = UserRepositoryImpl(),
) : ViewModel() {

    private val _user = MutableStateFlow<ApiResponse<User>>(ApiResponse.Loading)
    val user: StateFlow<ApiResponse<User>> = _user

    init {
        loadUserData()
    }

    fun loadUserData() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid == null) {
            _user.value = ApiResponse.Failure("Sesión no iniciada.")
            return
        }

        _user.value = ApiResponse.Loading
        viewModelScope.launch {
            try {
                repository.getUserData(uid).collectLatest { response ->
                    _user.value = response
                }
            } catch (e: Exception) {
                _user.value = ApiResponse.Failure(e.message ?: "Error al cargar los datos del perfil.")
            }
        }
    }

    fun updateUserData(name: String, phone: String, availability: String) {
        val current = (_user.value as? ApiResponse.Success)?.data ?: return
        val updatedUser = current.copy(
            name = name,
            phone = phone,
            availability = availability,
        )

        _user.value = ApiResponse.Loading
        viewModelScope.launch {
            try {
                repository.updateUser(updatedUser).collectLatest { response ->
                    _user.value = response
                }
            } catch (e: Exception) {
                _user.value = ApiResponse.Failure(e.message ?: "Error al actualizar el perfil.")
            }
        }
    }
}
