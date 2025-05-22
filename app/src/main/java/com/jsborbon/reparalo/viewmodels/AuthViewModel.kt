package com.jsborbon.reparalo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.AuthRepository
import com.jsborbon.reparalo.data.repository.UserRepository
import com.jsborbon.reparalo.models.User
import com.jsborbon.reparalo.models.UserType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _loginState = MutableStateFlow<ApiResponse<User>>(ApiResponse.Loading)
    val loginState: StateFlow<ApiResponse<User>> = _loginState

    private val _signUpState = MutableStateFlow<ApiResponse<User>>(ApiResponse.Loading)
    val signUpState: StateFlow<ApiResponse<User>> = _signUpState

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _resetState = MutableStateFlow<ApiResponse<Boolean>>(ApiResponse.Loading)
    val resetState: StateFlow<ApiResponse<Boolean>> = _resetState

    fun login(email: String, password: String) {
        _loginState.value = ApiResponse.Loading
        viewModelScope.launch {
            val firebaseUser = authRepository.signIn(email, password)
            if (firebaseUser != null) {
                val userData = authRepository.getUserData(firebaseUser.uid)
                if (userData != null) {
                    _user.value = userData
                    _loginState.value = ApiResponse.Success(userData)
                } else {
                    _loginState.value = ApiResponse.Failure("Fallo al obtener los datos del usuario.")
                }
            } else {
                _loginState.value = ApiResponse.Failure("Credenciales incorrectas o error de red.")
            }
        }
    }

    fun signUp(email: String, password: String, name: String, phone: String, userType: String) {
        _signUpState.value = ApiResponse.Loading
        viewModelScope.launch {
            val userTypeEnum = UserType.fromId(userType) ?: UserType.CLIENT

            val firebaseUser = authRepository.signUp(email, password, name, phone, userTypeEnum)
            if (firebaseUser != null) {
                val userData = authRepository.getUserData(firebaseUser.uid)
                if (userData != null) {
                    _user.value = userData
                    _signUpState.value = ApiResponse.Success(userData)
                } else {
                    _signUpState.value = ApiResponse.Failure(
                        "Registro completo, pero no se pudo" +
                            " obtener los datos del usuario.",
                    )
                }
            } else {
                _signUpState.value = ApiResponse.Failure(
                    "Registro fallido. Verifica tu conexión" +
                        " a Internet o intenta nuevamente.",
                )
            }
        }
    }

    fun updateUser(name: String, phone: String, availability: String) {
        viewModelScope.launch {
            val currentUser = _user.value ?: return@launch
            val updatedUser = currentUser.copy(
                name = name,
                phone = phone,
                availability = availability,
            )

            userRepository.updateUser(updatedUser).collectLatest { response ->
                if (response is ApiResponse.Success) {
                    _user.value = response.data
                }
            }
        }
    }

    fun changePassword(
        newPassword: String,
        onResult: (ApiResponse<Boolean>) -> Unit,
    ) {
        viewModelScope.launch {
            try {
                authRepository.updatePassword(newPassword)
                onResult(ApiResponse.Success(true))
            } catch (e: Exception) {
                onResult(ApiResponse.Failure(e.message ?: "Error al cambiar la contraseña."))
            }
        }
    }

    fun logout() {
        authRepository.signOut()
        _user.value = null
        _loginState.value = ApiResponse.Loading
        _signUpState.value = ApiResponse.Loading
    }
}
