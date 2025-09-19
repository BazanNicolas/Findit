package com.products.app.data.repository

import com.products.app.core.AppResult
import com.products.app.core.NetworkErrorHandler
import com.products.app.data.mapper.toDomain
import com.products.app.data.remote.AutosuggestApi
import com.products.app.domain.repository.AutosuggestRepository
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named

class AutosuggestRepositoryImpl @Inject constructor(
    @Named("autosuggest") private val retrofit: Retrofit,
    private val errorHandler: NetworkErrorHandler
) : AutosuggestRepository {

    override suspend fun getSuggestions(query: String): AppResult<List<com.products.app.domain.model.SearchSuggestion>> = try {
        val trimmedQuery = query.trim()
        if (trimmedQuery.isEmpty()) {
            AppResult.Success(emptyList())
        } else {
            val api = retrofit.create(AutosuggestApi::class.java)
            val dto = api.getAutosuggest(query = trimmedQuery)
            val suggestions = dto.toDomain()
            AppResult.Success(suggestions)
        }
    } catch (e: Exception) {
        AppResult.Error(errorHandler.handleError(e))
    }
}
