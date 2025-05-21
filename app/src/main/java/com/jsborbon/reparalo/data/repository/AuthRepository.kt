package com.jsborbon.reparalo.data.repository

import com.google.firebase.auth.FirebaseUser
import com.jsborbon.reparalo.models.User
import com.jsborbon.reparalo.models.UserType

interface AuthRepository {
    suspend fun signIn(email: String, password: String): FirebaseUser?
    suspend fun signUp(email: String, password: String, name: String, phone: String, userType: UserType): FirebaseUser?
    suspend fun getUserData(uid: String): User?
    suspend fun updatePassword(email: String): Boolean
    fun signOut()
}
