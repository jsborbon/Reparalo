package com.jsborbon.reparalo.data.api

import com.google.firebase.auth.FirebaseAuth
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = try {
            firebaseAuth.currentUser
                ?.getIdToken(false)
                ?.result
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
