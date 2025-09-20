package com.products.app.data.remote

import com.products.app.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

/**
 * OkHttp interceptor for adding authentication headers to API requests.
 * 
 * This interceptor automatically adds the Bearer token authorization header
 * to all outgoing requests to the MercadoLibre API. The token is retrieved
 * from the BuildConfig which is populated from local.properties during build.
 * 
 * The interceptor validates that the token is properly configured and throws
 * an exception if the token is missing or blank.
 */
class AuthInterceptor : Interceptor {
    
    /**
     * Intercepts outgoing requests and adds the authorization header.
     * 
     * @param chain The interceptor chain
     * @return Response with the authorization header added
     * @throws IllegalArgumentException if the access token is not configured
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = getAccessToken()
        
        val req = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
            
        return chain.proceed(req)
    }
    
    /**
     * Retrieves the access token from BuildConfig.
     * 
     * The token is injected at build time from the local.properties file.
     * This method validates that the token is properly configured.
     * 
     * @return The access token for API authentication
     * @throws IllegalArgumentException if the token is missing or blank
     */
    private fun getAccessToken(): String {
        val token = BuildConfig.ML_ACCESS_TOKEN
        require(!token.isBlank()) { "ML_ACCESS_TOKEN not configured in local.properties" }
        return token
    }
}