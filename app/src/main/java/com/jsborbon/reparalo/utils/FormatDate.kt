package com.jsborbon.reparalo.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatDate(dateString: String): String {
    return parseDate(dateString)?.let { formatDate(it) } ?: "Fecha inválida"
}

fun formatDate(timestamp: Long): String {
    return try {
        formatDate(Date(timestamp))
    } catch (_: Exception) {
        "Fecha inválida"
    }
}

fun formatDate(date: Date): String {
    return try {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        formatter.format(date)
    } catch (_: Exception) {
        "Fecha inválida"
    }
}

private fun parseDate(dateString: String): Date? {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        inputFormat.parse(dateString)
    } catch (_: Exception) {
        null
    }
}
