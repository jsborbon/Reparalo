package com.jsborbon.reparalo.data.api

sealed class ApiResponse<out T> {
    data class Success<out T>(
        val data: T,
    ) : ApiResponse<T>()

    data class Error(
        val errorMessage: String,
        val code: Int? = null,
    ) : ApiResponse<Nothing>()

    object Loading : ApiResponse<Nothing>()

    companion object {
        fun <T> loading(): ApiResponse<T> = Loading as ApiResponse<T>

        fun <T> success(data: T): ApiResponse<T> = Success(data)

        fun <T> error(
            message: String,
            code: Int? = null,
        ): ApiResponse<T> = Error(message, code)
    }
}
