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
    private val repository: UserRepository = UserRepositoryImpl()
) : ViewModel() {

    private val _user = MutableStateFlow<ApiResponse<User>>(ApiResponse.Loading)
    val user: StateFlow<ApiResponse<User>> = _user

    private val currentUid: String? = FirebaseAuth.getInstance().currentUser?.uid

    init {
        loadUserData()
    }

    fun loadUserData() {
        _user.value = ApiResponse.Loading
        viewModelScope.launch {
            val uid = currentUid
            if (uid != null) {
                val data = repository.getUserData(uid)
                if (data != null) {
                    _user.value = ApiResponse.Success(data)
                } else {
                    _user.value = ApiResponse.Failure("No se pudo cargar el perfil del usuario.")
                }
            } else {
                _user.value = ApiResponse.Failure("Sesión no iniciada.")
            }
        }
    }

    fun updateUserData(name: String, phone: String, availability: String) {
        val current = (_user.value as? ApiResponse.Success)?.data ?: return
        val updatedUser = current.copy(
            name = name,
            phone = phone,
            availability = availability
        )

        viewModelScope.launch {
            _user.value = ApiResponse.Loading
            repository.updateUser(updatedUser).collectLatest { response ->
                _user.value = response
            }
        }
    }
}
