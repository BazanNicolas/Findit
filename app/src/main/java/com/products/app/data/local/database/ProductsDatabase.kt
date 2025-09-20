package com.products.app.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.products.app.data.local.dao.SearchHistoryDao
import com.products.app.data.local.dao.ViewedProductDao
import com.products.app.data.local.entity.SearchHistoryEntity
import com.products.app.data.local.entity.ViewedProductEntity

/**
 * Room database for storing local product-related data.
 * 
 * This database contains tables for search history and viewed products,
 * enabling offline functionality and improved user experience. The database
 * uses Room's abstraction layer over SQLite for type-safe database access.
 * 
 * @see SearchHistoryEntity for search history data structure
 * @see ViewedProductEntity for viewed products data structure
 * @see SearchHistoryDao for search history data operations
 * @see ViewedProductDao for viewed products data operations
 */
@Database(
    entities = [SearchHistoryEntity::class, ViewedProductEntity::class],
    version = 2,
    exportSchema = false
)
abstract class ProductsDatabase : RoomDatabase() {
    
    /**
     * Provides access to search history data operations.
     * 
     * @return DAO for search history CRUD operations
     */
    abstract fun searchHistoryDao(): SearchHistoryDao
    
    /**
     * Provides access to viewed products data operations.
     * 
     * @return DAO for viewed products CRUD operations
     */
    abstract fun viewedProductDao(): ViewedProductDao
    
    companion object {
        @Volatile
        private var INSTANCE: ProductsDatabase? = null
        
        /**
         * Gets the singleton instance of the ProductsDatabase.
         * 
         * Uses double-checked locking pattern to ensure thread-safe singleton creation.
         * The database is created with destructive migration disabled to preserve data.
         * 
         * @param context The application context
         * @return The singleton database instance
         */
        fun getDatabase(context: Context): ProductsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProductsDatabase::class.java,
                    "products_database"
                )
                    .fallbackToDestructiveMigration(false)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
