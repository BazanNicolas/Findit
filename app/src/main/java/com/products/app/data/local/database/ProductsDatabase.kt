package com.products.app.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.products.app.data.local.dao.SearchHistoryDao
import com.products.app.data.local.dao.ViewedProductDao
import com.products.app.data.local.entity.SearchHistoryEntity
import com.products.app.data.local.entity.ViewedProductEntity

@Database(
    entities = [SearchHistoryEntity::class, ViewedProductEntity::class],
    version = 2,
    exportSchema = false
)
abstract class ProductsDatabase : RoomDatabase() {
    
    abstract fun searchHistoryDao(): SearchHistoryDao
    abstract fun viewedProductDao(): ViewedProductDao
    
    companion object {
        @Volatile
        private var INSTANCE: ProductsDatabase? = null
        
        fun getDatabase(context: Context): ProductsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProductsDatabase::class.java,
                    "products_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
