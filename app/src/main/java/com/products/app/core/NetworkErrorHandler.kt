package com.products.app.core

import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkErrorHandler @Inject constructor() {
    
    fun handleError(throwable: Throwable): String {
        return when (throwable) {
            is HttpException -> handleHttpException(throwable)
            is UnknownHostException -> "No internet connection. Please check your network settings."
            is IOException -> "Network error. Please check your connection and try again."
            is SecurityException -> "Permission denied. Please check app permissions."
            else -> throwable.message ?: "An unexpected error occurred. Please try again."
        }
    }
    
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
    
    fun isNetworkError(throwable: Throwable): Boolean {
        return when (throwable) {
            is HttpException -> true
            is UnknownHostException -> true
            is IOException -> true
            else -> false
        }
    }
    
    fun isRetryableError(throwable: Throwable): Boolean {
        return when (throwable) {
            is HttpException -> throwable.code() in listOf(408, 429, 500, 502, 503, 504)
            is UnknownHostException -> true
            is IOException -> true
            else -> false
        }
    }
}
