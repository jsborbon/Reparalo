package com.jsborbon.reparalo.data.repository.impl

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.jsborbon.reparalo.data.repository.AuthRepository
import com.jsborbon.reparalo.models.User
import com.jsborbon.reparalo.models.UserType
import kotlinx.coroutines.tasks.await
import java.util.Date

class AuthRepositoryImpl(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
) : AuthRepository {

    private val TAG = AuthRepositoryImpl::class.java.simpleName

    override suspend fun signIn(email: String, password: String): FirebaseUser? {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user ?: auth.currentUser
        } catch (e: Exception) {
            Log.e(TAG, "signIn failed", e)
            null
        }
    }

    override suspend fun signUp(
        email: String,
        password: String,
        name: String,
        phone: String,
        userType: UserType,
    ): FirebaseUser? {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user != null) {
                val registrationDate = Date()
                val profile = User(
                    uid = user.uid,
                    name = name,
                    email = email,
                    phone = phone,
                    userType = userType,
                    registrationDate = registrationDate,
                )
                firestore.collection("users").document(user.uid).set(profile).await()
                firestore.collection("users").document(user.uid)
                    .update("favorites", emptyList<String>()).await()
            }
            user
        } catch (e: Exception) {
            Log.e(TAG, "signUp failed", e)
            null
        }
    }

    override suspend fun getUserData(uid: String): User? {
        return try {
            val snapshot = firestore.collection("users").document(uid).get().await()
            snapshot.toObject(User::class.java)
        } catch (e: Exception) {
            Log.e(TAG, "getUserData failed", e)
            null
        }
    }

    override suspend fun updatePassword(newPassword: String): Boolean {
        return try {
            auth.currentUser?.updatePassword(newPassword)?.await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "updatePassword failed", e)
            false
        }
    }

    override suspend fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }

    override fun signOut() {
        auth.signOut()
    }
}
