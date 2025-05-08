package com.jsborbon.reparalo.data.api

import com.google.firebase.auth.FirebaseAuth
import okhttp3.Interceptor
import okhttp3.Response

/**
 * An [Interceptor] that adds the Firebase Authentication token
 * to every outgoing HTTP request, if available.
 */
class AuthInterceptor(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = try {
            firebaseAuth.currentUser
                ?.getIdToken(false) // Do not force refresh
                ?.result            // May block until the token is available
                ?.token
        } catch (e: Exception) {
            null
        }

        val authenticatedRequest = chain.request().newBuilder().apply {
            token?.let {
                header("Authorization", "Bearer $it")
            }
        }.build()

        return chain.proceed(authenticatedRequest)
    }
}
