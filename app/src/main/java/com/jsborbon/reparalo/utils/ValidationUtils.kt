package com.jsborbon.reparalo.utils

import android.util.Patterns
import com.jsborbon.reparalo.screens.profile.components.ValidationResult


object ValidationUtils {

    fun validateTitle(title: String): String? {
        return when {
            title.isBlank() -> "El título es obligatorio"
            title.length < 5 -> "El título es demasiado corto"
            title.length > 100 -> "El título es demasiado largo"
            else -> null
        }
    }

    fun validateDescription(description: String): String? {
        return when {
            description.isBlank() -> "La descripción es obligatoria"
            description.length < 20 -> "La descripción es demasiado corta"
            else -> null
        }
    }

    fun validateCategory(category: String): String? {
        return when {
            category.isBlank() -> "La categoría es obligatoria"
            else -> null
        }
    }


    fun validateName(name: String): ValidationResult {
        return when {
            name.isBlank() -> ValidationResult(false, "El nombre es obligatorio")
            name.length < 2 -> ValidationResult(false, "El nombre debe tener al menos 2 caracteres")
            name.length > 50 -> ValidationResult(false, "El nombre no puede exceder 50 caracteres")
            !name.matches(Regex("^[a-zA-ZáéíóúñÁÉÍÓÚÑ\\s]+$")) -> ValidationResult(false, "El nombre solo puede contener letras y espacios")
            else -> ValidationResult(true)
        }
    }

     fun validateEmail(email: String): ValidationResult {
        return when {
            email.isBlank() -> ValidationResult(false, "El email es obligatorio")
            !email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) ->
                ValidationResult(false, "Formato de email inválido")
            else -> ValidationResult(true)
        }
    }

     fun validatePhone(phone: String): ValidationResult {
        return when {
            phone.isBlank() -> ValidationResult(false, "El teléfono es obligatorio")
            phone.length < 9 -> ValidationResult(false, "El teléfono debe tener al menos 9 dígitos")
            !phone.matches(Regex("^[+]?[0-9\\s-()]+$")) ->
                ValidationResult(false, "Formato de teléfono inválido")
            else -> ValidationResult(true)
        }
    }

    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isStrongPassword(password: String): Boolean {
        return password.matches(Regex("^(?=.*[A-Z])(?=.*\\d).{6,}$"))
    }

    fun isValidPhone(phone: String): Boolean {
        return phone.matches(Regex("^\\d{7,}$"))
    }

    fun passwordsMatch(password1: String, password2: String): Boolean {
        return password1 == password2
    }

    fun areNotEmpty(vararg fields: String): Boolean {
        return fields.all { it.isNotBlank() }
    }
}
