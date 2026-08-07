package com.example.eSewaMarket.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import com.example.eSewaMarket.data.local.entity.FavouriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favourite: FavouriteEntity)

    @Query("""
        DELETE FROM favourite WHERE userId = :userId AND productId = :productId
    """
    )
    suspend fun removeFromFavourite(userId: Long, productId: Long)

    @Query("""
        SELECT * FROM favourite WHERE userId = :userId AND productId = :productId LIMIT 1
    """
    )
    suspend fun getFavouriteItem(userId: Long, productId: Long): FavouriteEntity?

    @Query("SELECT COUNT(*) FROM favourite WHERE userId = :userId")
    fun getFavouriteCount(userId: Long): Flow<Int>

    @Query("SELECT EXISTS(SELECT 1 FROM favourite WHERE userId = :userId AND productId = :productId)")
    fun isFavourite(userId: Long, productId: Long): Flow<Boolean>
}