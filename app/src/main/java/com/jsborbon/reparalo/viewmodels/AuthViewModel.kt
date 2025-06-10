package com.jsborbon.reparalo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.AuthRepository
import com.jsborbon.reparalo.data.repository.impl.AuthRepositoryImpl
import com.jsborbon.reparalo.models.User
import com.jsborbon.reparalo.models.UserType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel(
    private val authRepository: AuthRepository = AuthRepositoryImpl()
) : ViewModel() {

    private val _loginState = MutableStateFlow<ApiResponse<User>>(ApiResponse.Idle)
    val loginState: StateFlow<ApiResponse<User>> = _loginState

    private val _signUpState = MutableStateFlow<ApiResponse<User>>(ApiResponse.Idle)
    val signUpState: StateFlow<ApiResponse<User>> = _signUpState

    private val _resetState = MutableStateFlow<ApiResponse<Boolean>>(ApiResponse.Idle)
    val resetState: StateFlow<ApiResponse<Boolean>> = _resetState

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    fun login(email: String, password: String) {
        _loginState.value = ApiResponse.Loading
        viewModelScope.launch {
            try {
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
            } catch (e: Exception) {
                _loginState.value = ApiResponse.Failure(
                    e.message ?: "Error desconocido al iniciar sesión."
                )
            }
        }
    }

    fun signUp(
        email: String,
        password: String,
        name: String,
        phone: String,
        userType: String
    ) {
        _signUpState.value = ApiResponse.Loading
        viewModelScope.launch {
            try {
                val userTypeEnum = UserType.fromId(userType) ?: UserType.CLIENT
                val firebaseUser = authRepository.signUp(email, password, name, phone, userTypeEnum)
                if (firebaseUser != null) {
                    val userData = authRepository.getUserData(firebaseUser.uid)
                    if (userData != null) {
                        _user.value = userData
                        _signUpState.value = ApiResponse.Success(userData)
                    } else {
                        _signUpState.value = ApiResponse.Failure(
                            "Registro completo, pero no se pudo obtener los datos del usuario."
                        )
                    }
                } else {
                    _signUpState.value = ApiResponse.Failure(
                        "Registro fallido. Verifica tu conexión a Internet o intenta nuevamente."
                    )
                }
            } catch (e: FirebaseAuthUserCollisionException) {
                _signUpState.value = ApiResponse.Failure("El correo electrónico ya está registrado.")
            } catch (e: Exception) {
                _signUpState.value = ApiResponse.Failure(
                    e.message ?: "Error desconocido al registrar."
                )
            }
        }
    }

    fun changePassword(currentPassword: String, newPassword: String) {
        _resetState.value = ApiResponse.Loading

        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email
        if (user != null && email != null) {
            val credential = EmailAuthProvider.getCredential(email, currentPassword)
            viewModelScope.launch {
                try {
                    user.reauthenticate(credential).await()
                    user.updatePassword(newPassword).await()
                    _resetState.value = ApiResponse.Success(true)
                } catch (e: Exception) {
                    _resetState.value = ApiResponse.Failure(
                        e.message ?: "Error al cambiar la contraseña."
                    )
                }
            }
        } else {
            _resetState.value = ApiResponse.Failure("Usuario no autenticado.")
        }
    }

    fun resetPassword(email: String) {
        _resetState.value = ApiResponse.Loading
        viewModelScope.launch {
            try {
                authRepository.resetPassword(email)
                _resetState.value = ApiResponse.Success(true)
            } catch (e: Exception) {
                _resetState.value = ApiResponse.Failure(
                    e.message ?: "Error al enviar el correo de recuperación."
                )
            }
        }
    }

    fun changePassword(
        currentPassword: String,
        newPassword: String,
        onResult: (ApiResponse<Boolean>) -> Unit
    ) {
        viewModelScope.launch {
            try {
                authRepository.reauthenticateAndChangePassword(currentPassword, newPassword)
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

    fun resetSignUpState() {
        _signUpState.value = ApiResponse.Idle
    }

    fun resetLoginState() {
        _loginState.value = ApiResponse.Idle
    }

    fun resetPasswordState() {
        _resetState.value = ApiResponse.Idle
    }
}
