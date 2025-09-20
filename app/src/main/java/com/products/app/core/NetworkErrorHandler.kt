package com.products.app.core

import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handles network-related errors and provides user-friendly error messages.
 * 
 * This class centralizes error handling logic for network operations,
 * converting technical exceptions into meaningful user messages. It handles
 * various types of network errors including HTTP errors, connectivity issues,
 * and security exceptions.
 * 
 * The error messages are designed to be user-friendly and actionable,
 * helping users understand what went wrong and how to potentially fix it.
 */
@Singleton
class NetworkErrorHandler @Inject constructor() {
    
    /**
     * Converts a throwable into a user-friendly error message.
     * 
     * This method categorizes different types of exceptions and returns
     * appropriate error messages for each category. HTTP exceptions are
     * handled separately to provide specific error messages based on status codes.
     * 
     * @param throwable The exception that occurred
     * @return A localized error message suitable for display to the user
     */
    fun handleError(throwable: Throwable): String {
        return when (throwable) {
            is HttpException -> handleHttpException(throwable)
            is UnknownHostException -> "No internet connection. Please check your network settings."
            is IOException -> "Network error. Please check your connection and try again."
            is SecurityException -> "Permission denied. Please check app permissions."
            else -> throwable.message ?: "An unexpected error occurred. Please try again."
        }
    }
    
    /**
     * Handles HTTP-specific exceptions and provides appropriate error messages.
     * 
     * Maps HTTP status codes to user-friendly error messages that explain
     * what went wrong and suggest potential solutions.
     * 
     * @param httpException The HTTP exception containing the status code
     * @return A user-friendly error message based on the HTTP status code
     */
    private fun handleHttpException(httpException: HttpException): String {
        return when (httpException.code()) {
            400 -> "Invalid request. Please check your input and try again."
            401 -> "Authentication failed. Please try again."
            403 -> "Access denied. You don't have permission to access this resource."
            404 -> "The requested resource was not found."
            408 -> "Request timeout. Please try again."
            429 -> "Too many requests. Please wait a moment and try again."
            500 -> "Server error. Please try again later."
            502 -> "Bad gateway. The server is temporarily unavailable."
            503 -> "Service unavailable. Please try again later."
            504 -> "Gateway timeout. Please try again."
            else -> "Network error (${httpException.code()}). Please try again."
        }
    }
    
    /**
     * Determines if the given throwable represents a network-related error.
     * 
     * This method is useful for distinguishing between network errors and
     * other types of errors (like business logic errors) for different
     * handling strategies.
     * 
     * @param throwable The exception to check
     * @return true if the error is network-related, false otherwise
     */
    fun isNetworkError(throwable: Throwable): Boolean {
        return when (throwable) {
            is HttpException -> true
            is UnknownHostException -> true
            is IOException -> true
            else -> false
        }
    }
    
    /**
     * Determines if the given error is retryable.
     * 
     * Some errors (like temporary network issues or server overload)
     * can be retried, while others (like authentication failures)
     * should not be retried automatically.
     * 
     * @param throwable The exception to check
     * @return true if the error can be retried, false otherwise
     */
    fun isRetryableError(throwable: Throwable): Boolean {
        return when (throwable) {
            is HttpException -> throwable.code() in listOf(408, 429, 500, 502, 503, 504)
            is UnknownHostException -> true
            is IOException -> true
            else -> false
        }
    }
}
