package com.products.app.data.remote

import com.products.app.data.remote.dto.SimpleAutosuggestDto
import retrofit2.http.GET
import retrofit2.http.Query

interface AutosuggestApi {
    @GET("resources/sites/MLA/autosuggest")
    suspend fun getAutosuggest(
        @Query("q") query: String,
        @Query("showFilters") showFilters: Boolean = true,
        @Query("limit") limit: Int = 6,
        @Query("api_version") apiVersion: Int = 2
    ): SimpleAutosuggestDto
}
