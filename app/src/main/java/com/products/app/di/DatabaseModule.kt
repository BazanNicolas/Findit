package com.products.app.di

import android.content.Context
import com.products.app.data.local.dao.SearchHistoryDao
import com.products.app.data.local.database.ProductsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideProductsDatabase(@ApplicationContext context: Context): ProductsDatabase {
        return ProductsDatabase.getDatabase(context)
    }
    
    @Provides
    fun provideSearchHistoryDao(database: ProductsDatabase): SearchHistoryDao {
        return database.searchHistoryDao()
    }
}
