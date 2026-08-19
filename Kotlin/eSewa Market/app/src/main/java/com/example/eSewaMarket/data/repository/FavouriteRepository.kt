package com.example.eSewaMarket.data.repository

import com.example.eSewaMarket.data.api.ApiService
import com.example.eSewaMarket.data.local.dao.FavouriteDao
import com.example.eSewaMarket.data.local.dao.ProductDao
import com.example.eSewaMarket.data.local.entity.FavouriteEntity
import com.example.eSewaMarket.data.local.entity.ProductEntity
import com.example.eSewaMarket.data.models.AddToCartRequest
import com.example.eSewaMarket.data.models.FavouriteResponse
import com.example.eSewaMarket.data.models.FavouriteToggles
import com.example.eSewaMarket.data.models.Product
import com.example.eSewaMarket.data.models.ProductResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await

class FavouriteRepository(
    private val favouriteDao: FavouriteDao,
    private val productDao: ProductDao,
    private val userRepository: UserSessionRepository,
    private val apiService: ApiService
) {
    private suspend fun currentUserId(): Long {
        return userRepository.user.first().id
    }

    suspend fun toggleFavourite(product: Product) {
        val userId = currentUserId()

        val item = favouriteDao.getFavouriteItem(userId, product.id)

        if (item == null) {
            productDao.insertIntoProducts(
                listOf(
                    ProductEntity(
                        productId = product.id,
                        title = product.title,
                        thumbnail = product.thumbnail,
                        price = product.price,
                        brand = product.brand
                    )
                )
            )

            favouriteDao.insert(
                FavouriteEntity(
                    userId = userId,
                    productId = product.id
                )
            )
        } else {
            favouriteDao.removeFromFavourite(userId, product.id)
        }

        val token = FirebaseAuth.getInstance()
            .currentUser
            ?.getIdToken(false)
            ?.await()
            ?.token
            ?: throw IllegalStateException("User is not authenticated")

        apiService.toggleFavourite(
            "Bearer $token",
            FavouriteToggles(productId = product.id)
        )
    }

    fun totalQuantity() = userRepository.user.flatMapLatest { user ->
        favouriteDao.getFavouriteCount(user.id)
    }

    fun isFavourite(productId: Long): Flow<Boolean> {
        return userRepository.user.flatMapLatest { user ->
            favouriteDao.isFavourite(user.id, productId)
        }
    }

    suspend fun syncFavourites(
        userId: Long,
        serverFavourites: List<FavouriteEntity>
    ) {
        if (serverFavourites.isEmpty()) {
            favouriteDao.clearFavourites(userId)
            return
        }

        val serverProductIds =
            serverFavourites.map { it.productId }

        favouriteDao.deleteNotInServer(
            userId = userId,
            serverProductIds = serverProductIds
        )

        favouriteDao.insertAll(serverFavourites)
    }

    fun favouriteProducts() : Flow<List<FavouriteResponse>>{
        return userRepository.user.flatMapLatest { user ->
            favouriteDao.getFavouriteProducts(user.id)
        }
    }

    suspend fun deleteFavourites(){
        val userId = currentUserId()
        val favourites = favouriteDao.getFavouriteIds(userId)

        val token = FirebaseAuth.getInstance()
            .currentUser
            ?.getIdToken(false)
            ?.await()
            ?.token
            ?: throw IllegalStateException("User is not authenticated")

        favourites.forEach { productId ->
            apiService.toggleFavourite(
                "Bearer $token",
                FavouriteToggles(productId = productId)
            )
        }

        favouriteDao.clearFavourites(userId)
    }
}