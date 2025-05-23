package com.jsborbon.reparalo.data.repository.impl

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.jsborbon.reparalo.data.repository.AuthRepository
import com.jsborbon.reparalo.models.User
import com.jsborbon.reparalo.models.UserType
import kotlinx.coroutines.tasks.await
import java.time.Instant

class AuthRepositoryImpl(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : AuthRepository {

    private val TAG = "AuthRepositoryImpl"

    override suspend fun signIn(email: String, password: String): FirebaseUser? {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            auth.currentUser
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
        userType: UserType
    ): FirebaseUser? {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = auth.currentUser

            firebaseUser?.let { user ->
                val registrationDate = Instant.now().toString()
                val profile = User(
                    uid = user.uid,
                    name = name,
                    email = email,
                    phone = phone,
                    userType = userType,
                    registrationDate = registrationDate
                )
                firestore.collection("users").document(user.uid).set(profile).await()
            }

            firebaseUser
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

    override fun signOut() {
        auth.signOut()
    }
}
