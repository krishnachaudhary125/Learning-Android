package com.example.eSewaMarket.data.local

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.example.eSewaMarket.data.local.dao.CartDao
import com.example.eSewaMarket.data.local.entity.CartEntity

@Database(
    entities = [
        CartEntity::class
    ],
    version = 1
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun cartDao(): CartDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "esewa_market_db"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}