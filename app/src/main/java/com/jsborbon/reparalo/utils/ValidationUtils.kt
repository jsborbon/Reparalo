package com.jsborbon.reparalo.utils

import android.util.Patterns

object ValidationUtils {

    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isStrongPassword(password: String): Boolean {
        // At least 6 characters, one number, one uppercase letter
        val regex = Regex("^(?=.*[A-Z])(?=.*[0-9]).{6,}$")
        return regex.matches(password)
    }

    fun isValidPhone(phone: String): Boolean {
        // Minimum 7 digits, only numbers
        return phone.matches(Regex("^\\d{7,}$"))
    }

    fun passwordsMatch(password1: String, password2: String): Boolean {
        return password1 == password2
    }

    fun areNotEmpty(vararg fields: String): Boolean {
        return fields.all { it.isNotBlank() }
    }
}
