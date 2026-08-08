package com.example.eSewaMarket.data.local

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.example.eSewaMarket.data.local.dao.CartDao
import com.example.eSewaMarket.data.local.dao.FavouriteDao
import com.example.eSewaMarket.data.local.entity.CartEntity
import com.example.eSewaMarket.data.local.entity.FavouriteEntity

@Database(
    entities = [
        CartEntity::class,
        FavouriteEntity::class
    ],
    version = 1
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun cartDao(): CartDao
    abstract fun favouriteDao(): FavouriteDao
}