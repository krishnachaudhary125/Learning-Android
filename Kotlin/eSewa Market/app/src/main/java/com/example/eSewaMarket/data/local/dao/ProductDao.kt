package com.example.eSewaMarket.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import com.example.eSewaMarket.data.local.entity.ProductEntity

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIntoProducts(products: List<ProductEntity>)

    @Query("""
        DELETE FROM products WHERE productId NOT IN (:serverProductId)
    """
    )
    suspend fun deleteFromProducts(serverProductId: List<Long>)
}