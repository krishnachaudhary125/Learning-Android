package com.example.eSewaMarket.data.local.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import com.example.eSewaMarket.data.local.entity.CartEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cart: CartEntity)

    @Query("""
        UPDATE cart SET quantity = :quantity WHERE userId = :userId AND productId = :productId
    """
    )
    suspend fun updateQuantity(userId: Long, productId: Long, quantity: Int)

    @Query("""
        DELETE FROM cart WHERE userId = :userId AND productId = :productId
    """
    )
    suspend fun removeFromCart(userId: Long, productId: Long)

    @Query("""
        SELECT * FROM cart WHERE userId = :userId AND productId = :productId
    """
    )
    suspend fun getCartItem(userId: Long, productId: Long): CartEntity?

    @Query("""
        SELECT quantity FROM cart WHERE userId = :userId AND productId = :productId
    """
    )
    fun getProductQuantity(userId: Long, productId: Long): Flow<Int>

    @Query("""
        SELECT COALESCE(SUM(quantity), 0) FROM cart WHERE userId = :userId
    """
    )
    fun getTotalQuantity(userId: Long): Flow<Int>
}