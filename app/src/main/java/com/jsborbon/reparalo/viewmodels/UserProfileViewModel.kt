package com.jsborbon.reparalo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.jsborbon.reparalo.data.api.ApiResponse
import com.jsborbon.reparalo.data.repository.UserRepository
import com.jsborbon.reparalo.data.repository.impl.UserRepositoryImpl
import com.jsborbon.reparalo.models.AvailabilitySlot
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

    fun updateUserData(name: String, phone: String, availabilityString: String) {
        val current = (_user.value as? ApiResponse.Success)?.data ?: return
        val parsedAvailability = parseAvailabilityString(availabilityString)

        val updatedUser = current.copy(
            name = name,
            phone = phone,
            availability = parsedAvailability
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

    // Utility to convert user input string to a list of structured availability slots
    private fun parseAvailabilityString(input: String): List<AvailabilitySlot> {
        return input.split(",")
            .mapNotNull { entry ->
                val trimmed = entry.trim()
                val parts = trimmed.split(" ")

                if (parts.size != 2) return@mapNotNull null

                val day = parts[0].replaceFirstChar { it.uppercaseChar() }
                val times = parts[1].split("-")

                if (times.size != 2) return@mapNotNull null

                val startTime = times[0].trim()
                val endTime = times[1].trim()

                AvailabilitySlot(
                    day = day,
                    startTime = startTime,
                    endTime = endTime
                )
            }
    }
}
