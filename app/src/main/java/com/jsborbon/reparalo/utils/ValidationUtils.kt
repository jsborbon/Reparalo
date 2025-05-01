package com.jsborbon.reparalo.utils

import android.util.Patterns

object ValidationUtils {

    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isStrongPassword(password: String): Boolean {
        // Al menos 6 caracteres, un número, una letra mayúscula
        val regex = Regex("^(?=.*[A-Z])(?=.*[0-9]).{6,}")
        return regex.matches(password)
    }

    fun isValidPhone(phone: String): Boolean {
        // Validar mínimo 7 dígitos y solo números
        return phone.matches(Regex("^\\d{7,}\$"))
    }

    fun passwordsMatch(pass1: String, pass2: String): Boolean {
        return pass1 == pass2
    }

    fun isNotEmpty(vararg fields: String): Boolean {
        return fields.all { it.isNotBlank() }
    }
}
