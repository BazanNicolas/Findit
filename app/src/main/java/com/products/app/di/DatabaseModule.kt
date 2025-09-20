package com.products.app.di

import android.content.Context
import com.products.app.data.local.dao.SearchHistoryDao
import com.products.app.data.local.dao.ViewedProductDao
import com.products.app.data.local.database.ProductsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing database-related dependencies.
 * 
 * This module provides the Room database instance and its associated DAOs
 * for dependency injection. It ensures that the database is created as a
 * singleton and provides access to the DAOs for data operations.
 * 
 * The module is installed in SingletonComponent to ensure that the database
 * instance is shared across the entire application lifecycle.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    /**
     * Provides the ProductsDatabase instance as a singleton.
     * 
     * The database is created using the singleton pattern implemented in
     * ProductsDatabase.getDatabase() to ensure only one instance exists
     * throughout the application lifecycle.
     * 
     * @param context The application context
     * @return The singleton ProductsDatabase instance
     */
    @Provides
    @Singleton
    fun provideProductsDatabase(@ApplicationContext context: Context): ProductsDatabase {
        return ProductsDatabase.getDatabase(context)
    }
    
    /**
     * Provides the SearchHistoryDao for search history operations.
     * 
     * @param database The ProductsDatabase instance
     * @return The SearchHistoryDao for search history data access
     */
    @Provides
    fun provideSearchHistoryDao(database: ProductsDatabase): SearchHistoryDao {
        return database.searchHistoryDao()
    }
    
    /**
     * Provides the ViewedProductDao for viewed products operations.
     * 
     * @param database The ProductsDatabase instance
     * @return The ViewedProductDao for viewed products data access
     */
    @Provides
    fun provideViewedProductDao(database: ProductsDatabase): ViewedProductDao {
        return database.viewedProductDao()
    }
}
