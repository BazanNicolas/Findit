package com.products.app.data.remote

import com.products.app.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = getAccessToken()
        
        val req = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
            
        return chain.proceed(req)
    }
    
    private fun getAccessToken(): String {
        val token = BuildConfig.ML_ACCESS_TOKEN
        require(!token.isBlank()) { "ML_ACCESS_TOKEN not configured in local.properties" }
        return token
    }
}