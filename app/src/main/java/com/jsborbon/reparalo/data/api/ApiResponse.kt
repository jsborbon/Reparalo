package com.jsborbon.reparalo.data.api

/**
 * A sealed class representing the state of an API response.
 */
sealed class ApiResponse<out T> {

    /** Represents a loading state. */
    object Loading : ApiResponse<Nothing>()

    /** Represents a successful response with data of type [T]. */
    data class Success<out T>(
        val data: T
    ) : ApiResponse<T>()

    /** Represents a failed response with an error message and optional HTTP code. */
    data class Failure(
        val errorMessage: String,
        val code: Int? = null
    ) : ApiResponse<Nothing>()

    companion object {

        /** Builds a [Loading] state. */
        fun <T> loading(): ApiResponse<T> = Loading as ApiResponse<T>

        /** Builds a [Success] state with the provided [data]. */
        fun <T> success(data: T): ApiResponse<T> = Success(data)

        /** Builds a [Failure] state with the provided [message] and optional [code]. */
        fun <T> failure(
            message: String,
            code: Int? = null
        ): ApiResponse<T> = Failure(message, code)
    }
}
