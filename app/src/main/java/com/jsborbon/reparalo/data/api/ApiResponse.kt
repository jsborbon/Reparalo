package com.jsborbon.reparalo.data.api

sealed class ApiResponse<out T> {

    object Idle : ApiResponse<Nothing>()
    object Loading : ApiResponse<Nothing>()

    data class Success<out T>(
        val data: T,
    ) : ApiResponse<T>()

    data class Failure(
        val errorMessage: String,
        val code: Int? = null,
    ) : ApiResponse<Nothing>()

    companion object {
        fun <T> idle(): ApiResponse<T> = Idle as ApiResponse<T>
        fun <T> loading(): ApiResponse<T> = Loading as ApiResponse<T>
        fun <T> success(data: T): ApiResponse<T> = Success(data)
        fun <T> failure(message: String, code: Int? = null): ApiResponse<T> =
            Failure(message, code)
    }
}
