package com.products.app.di

import com.products.app.core.NetworkErrorHandler
import com.products.app.data.repository.AutosuggestRepositoryImpl
import com.products.app.data.repository.ProductDetailRepositoryImpl
import com.products.app.data.repository.ProductsRepositoryImpl
import com.products.app.data.repository.SearchHistoryRepositoryImpl
import com.products.app.data.repository.ViewedProductRepositoryImpl
import com.products.app.data.remote.AutosuggestApi
import com.products.app.data.remote.ProductsApi
import com.products.app.domain.repository.AutosuggestRepository
import com.products.app.domain.repository.ProductDetailRepository
import com.products.app.domain.repository.ProductsRepository
import com.products.app.domain.repository.SearchHistoryRepository
import com.products.app.domain.repository.ViewedProductRepository
import javax.inject.Named
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing repository implementations.
 * 
 * This module binds repository interfaces to their concrete implementations,
 * following the Repository pattern. It provides implementations for both
 * local data repositories (using Room DAOs) and remote data repositories
 * (using Retrofit APIs).
 * 
 * All repositories are provided as singletons to ensure consistent state
 * and efficient resource usage throughout the application.
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    
    /**
     * Provides the SearchHistoryRepository implementation.
     * 
     * This repository handles local search history operations using Room database.
     * It provides methods for saving, retrieving, and managing search queries.
     * 
     * @param searchHistoryDao The DAO for search history database operations
     * @return SearchHistoryRepository implementation
     */
    @Provides
    @Singleton
    fun provideSearchHistoryRepository(
        searchHistoryDao: com.products.app.data.local.dao.SearchHistoryDao
    ): SearchHistoryRepository =
        SearchHistoryRepositoryImpl(searchHistoryDao)
    
    /**
     * Provides the ProductDetailRepository implementation.
     * 
     * This repository handles detailed product information retrieval from the
     * MercadoLibre API. It includes error handling and data transformation.
     * 
     * @param productsApi The Retrofit API interface for product details
     * @param errorHandler The NetworkErrorHandler for processing API errors
     * @return ProductDetailRepository implementation
     */
    @Provides
    @Singleton
    fun provideProductDetailRepository(
        productsApi: com.products.app.data.remote.ProductsApi,
        errorHandler: NetworkErrorHandler
    ): ProductDetailRepository =
        ProductDetailRepositoryImpl(productsApi, errorHandler)
    
    /**
     * Provides the ViewedProductRepository implementation.
     * 
     * This repository handles local viewed products operations using Room database.
     * It provides methods for tracking and retrieving recently viewed products.
     * 
     * @param viewedProductDao The DAO for viewed products database operations
     * @return ViewedProductRepository implementation
     */
    @Provides
    @Singleton
    fun provideViewedProductRepository(
        viewedProductDao: com.products.app.data.local.dao.ViewedProductDao
    ): ViewedProductRepository =
        ViewedProductRepositoryImpl(viewedProductDao)
    
    /**
     * Provides the ProductsRepository implementation.
     * 
     * This repository handles product search operations from the MercadoLibre API.
     * It provides methods for searching products with pagination support.
     * 
     * @param productsApi The Retrofit API interface for product search
     * @param errorHandler The NetworkErrorHandler for processing API errors
     * @return ProductsRepository implementation
     */
    @Provides
    @Singleton
    fun provideProductsRepository(
        productsApi: ProductsApi,
        errorHandler: NetworkErrorHandler
    ): ProductsRepository =
        ProductsRepositoryImpl(productsApi, errorHandler)
    
    /**
     * Provides the AutosuggestRepository implementation.
     * 
     * This repository handles autocomplete suggestions from the MercadoLibre API.
     * It provides real-time search suggestions for improved user experience.
     * 
     * @param autosuggestApi The Retrofit API interface for autosuggest operations
     * @param errorHandler The NetworkErrorHandler for processing API errors
     * @return AutosuggestRepository implementation
     */
    @Provides
    @Singleton
    fun provideAutosuggestRepository(
        @Named("autosuggest") autosuggestApi: AutosuggestApi,
        errorHandler: NetworkErrorHandler
    ): AutosuggestRepository =
        AutosuggestRepositoryImpl(autosuggestApi, errorHandler)
}
