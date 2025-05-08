package com.jsborbon.reparalo.models

enum class UserType(val label: String) {
    CLIENT("Cliente"),
    TECHNICIAN("Técnico"),
    ADMIN("Administrador");

    companion object {
        fun fromId(id: String): UserType? = entries.find { it.name.equals(id, ignoreCase = true) }
    }
}
