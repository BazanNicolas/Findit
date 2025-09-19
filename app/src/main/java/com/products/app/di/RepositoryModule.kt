package com.products.app.di

import com.products.app.core.NetworkErrorHandler
import com.products.app.data.repository.ProductDetailRepositoryImpl
import com.products.app.data.repository.SearchHistoryRepositoryImpl
import com.products.app.data.repository.ViewedProductRepositoryImpl
import com.products.app.domain.repository.ProductDetailRepository
import com.products.app.domain.repository.SearchHistoryRepository
import com.products.app.domain.repository.ViewedProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    
    @Provides
    @Singleton
    fun provideSearchHistoryRepository(
        searchHistoryDao: com.products.app.data.local.dao.SearchHistoryDao
    ): SearchHistoryRepository =
        SearchHistoryRepositoryImpl(searchHistoryDao)
    
    @Provides
    @Singleton
    fun provideProductDetailRepository(
        productsApi: com.products.app.data.remote.ProductsApi,
        errorHandler: NetworkErrorHandler
    ): ProductDetailRepository =
        ProductDetailRepositoryImpl(productsApi, errorHandler)
    
    @Provides
    @Singleton
    fun provideViewedProductRepository(
        viewedProductDao: com.products.app.data.local.dao.ViewedProductDao
    ): ViewedProductRepository =
        ViewedProductRepositoryImpl(viewedProductDao)
}
