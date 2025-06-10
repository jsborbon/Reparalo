package com.jsborbon.reparalo.data.repository.impl

import android.util.Log
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.jsborbon.reparalo.data.repository.AuthRepository
import com.jsborbon.reparalo.models.User
import com.jsborbon.reparalo.models.UserType
import kotlinx.coroutines.tasks.await
import java.util.Date

class AuthRepositoryImpl(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
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
        userType: UserType
    ): FirebaseUser? {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user
            if (firebaseUser != null) {
                val registrationDate = Date()
                val newUser = User(
                    uid = firebaseUser.uid,
                    name = name,
                    email = email,
                    phone = phone,
                    userType = userType,
                    registrationDate = registrationDate
                )
                val userRef = firestore.collection("users").document(firebaseUser.uid)
                userRef.set(newUser).await()
                userRef.update("favorites", emptyList<String>()).await()
            }
            firebaseUser
        } catch (e: FirebaseAuthUserCollisionException) {
            Log.e(TAG, "El correo ya está en uso", e)
            throw FirebaseAuthUserCollisionException(
                e.errorCode,
                "El correo electrónico ya está registrado."
            )
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

    override suspend fun reauthenticateAndChangePassword(
        currentPassword: String,
        newPassword: String
    ) {
        try {
            val user = auth.currentUser
                ?: throw Exception("Usuario no autenticado.")

            val email = user.email
                ?: throw Exception("Correo electrónico no disponible.")

            val credential = EmailAuthProvider.getCredential(email, currentPassword)
            user.reauthenticate(credential).await()
            user.updatePassword(newPassword).await()
        } catch (e: Exception) {
            Log.e(TAG, "reauthenticateAndChangePassword failed", e)
            throw e
        }
    }

    override suspend fun resetPassword(email: String) {
        try {
            auth.sendPasswordResetEmail(email).await()
        } catch (e: Exception) {
            Log.e(TAG, "resetPassword failed", e)
            throw e
        }
    }

    override fun signOut() {
        auth.signOut()
    }
}
