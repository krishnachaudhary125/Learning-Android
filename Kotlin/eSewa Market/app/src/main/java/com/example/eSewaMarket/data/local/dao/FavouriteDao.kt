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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(favourites: List<FavouriteEntity>)

    @Query("""
        DELETE FROM favourite WHERE userId = :userId AND productId NOT IN (:serverProductIds)
    """
    )
    suspend fun deleteNotInServer(userId: Long, serverProductIds: List<Long>)

    @Query("""
        DELETE FROM favourite WHERE userId = :userId
    """
    )
    suspend fun clearFavourites(userId: Long)

    suspend fun syncFavourites(
        userId: Long,
        serverFavourites: List<FavouriteEntity>
    ) {

        if (serverFavourites.isEmpty()) {
            clearFavourites(userId)
            return
        }

        val serverProductIds =
            serverFavourites.map { it.productId }

        deleteNotInServer(
            userId = userId,
            serverProductIds = serverProductIds
        )

        insertAll(serverFavourites)
    }
}