package com.products.app.core

import android.content.Context
import com.products.app.R
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
class NetworkErrorHandler @Inject constructor(
    private val context: Context
) {
    
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
            is UnknownHostException -> context.getString(R.string.network_error_no_internet)
            is IOException -> context.getString(R.string.network_error_connection)
            is SecurityException -> context.getString(R.string.network_error_permission)
            else -> throwable.message ?: context.getString(R.string.network_error_unexpected)
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
            400 -> context.getString(R.string.network_error_invalid_request)
            401 -> context.getString(R.string.network_error_auth_failed)
            403 -> context.getString(R.string.network_error_access_denied)
            404 -> context.getString(R.string.network_error_not_found)
            408 -> context.getString(R.string.network_error_timeout)
            429 -> context.getString(R.string.network_error_too_many_requests)
            500 -> context.getString(R.string.network_error_server_error)
            502 -> context.getString(R.string.network_error_bad_gateway)
            503 -> context.getString(R.string.network_error_service_unavailable)
            504 -> context.getString(R.string.network_error_gateway_timeout)
            else -> context.getString(R.string.network_error_generic, httpException.code())
        }
    }
}
