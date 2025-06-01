package com.jsborbon.reparalo.utils

import android.util.Patterns

object ValidationUtils {

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
