package com.products.app.di

import com.products.app.data.repository.ProductDetailRepositoryImpl
import com.products.app.data.repository.SearchHistoryRepositoryImpl
import com.products.app.data.repository.ViewedProductRepositoryImpl
import com.products.app.domain.repository.ProductDetailRepository
import com.products.app.domain.repository.SearchHistoryRepository
import com.products.app.domain.repository.ViewedProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindSearchHistoryRepository(
        searchHistoryRepositoryImpl: SearchHistoryRepositoryImpl
    ): SearchHistoryRepository
    
    @Binds
    @Singleton
    abstract fun bindProductDetailRepository(
        productDetailRepositoryImpl: ProductDetailRepositoryImpl
    ): ProductDetailRepository
    
    @Binds
    @Singleton
    abstract fun bindViewedProductRepository(
        viewedProductRepositoryImpl: ViewedProductRepositoryImpl
    ): ViewedProductRepository
}
