package com.example.atry.data.repository

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

object PrefKeys{
    val CART_ITEMS = stringPreferencesKey("cart_items")
    val FAV_ITEMS = stringSetPreferencesKey("fav_items")
}

class ProductCountRepository(private val context: Context) {

    private val json = Json { ignoreUnknownKeys = true }

    private val cartMapFlow: Flow<Map<String, Int>> = context.dataStore.data
        .map { prefs ->
            val jsonString = prefs[PrefKeys.CART_ITEMS] ?: "{}"
            runCatching { json.decodeFromString<Map<String, Int>>(jsonString) }
                .getOrDefault(emptyMap())
        }

    val cartCount: Flow<Int> = cartMapFlow.map { it.values.sum() }

    suspend fun addToCart(productId: String) {
        Log.d("CartDebug", "Repository addToCart: $productId")

        context.dataStore.edit { prefs ->
            val currentMap = decodeCart(prefs[PrefKeys.CART_ITEMS])
            Log.d("CartDebug", "Before: $currentMap")

            val updatedMap = currentMap.toMutableMap()
            updatedMap[productId] = (updatedMap[productId] ?: 0) + 1

            Log.d("CartDebug", "After: $updatedMap")

            prefs[PrefKeys.CART_ITEMS] = json.encodeToString(updatedMap)
        }
    }

    suspend fun removeOneFromCart(productId: String) {
        context.dataStore.edit { prefs ->
            val currentMap = decodeCart(prefs[PrefKeys.CART_ITEMS])
            val updatedMap = currentMap.toMutableMap()
            val newQty = (updatedMap[productId] ?: 0) - 1
            if (newQty <= 0) {
                updatedMap.remove(productId)
            } else {
                updatedMap[productId] = newQty
            }
            prefs[PrefKeys.CART_ITEMS] = json.encodeToString(updatedMap)
        }
    }

    suspend fun removeAllOfProductFromCart(productId: String) {
        context.dataStore.edit { prefs ->
            val currentMap = decodeCart(prefs[PrefKeys.CART_ITEMS]).toMutableMap()
            currentMap.remove(productId)
            prefs[PrefKeys.CART_ITEMS] = json.encodeToString(currentMap)
        }
    }

    fun getQuantity(productId: String): Flow<Int> =
        cartMapFlow.map {
            it[productId] ?: 0
        }

    private fun decodeCart(jsonString: String?): Map<String, Int> {
        if (jsonString.isNullOrEmpty()) return emptyMap()
        return runCatching { json.decodeFromString<Map<String, Int>>(jsonString) }
            .getOrDefault(emptyMap())
    }

    val favouriteCount: Flow<Int> = context.dataStore.data
        .map { it[PrefKeys.FAV_ITEMS]?.size ?: 0 }

    suspend fun addToFav(productId: String){
        context.dataStore.edit { prefs ->
            val current = prefs[PrefKeys.FAV_ITEMS] ?: emptySet()
            prefs[PrefKeys.FAV_ITEMS] = current + productId
        }
    }

    suspend fun removeFromFavourite(productId: String){
        context.dataStore.edit { prefs ->
            val current = prefs[PrefKeys.FAV_ITEMS] ?: emptySet()
            prefs[PrefKeys.FAV_ITEMS] = current - productId
        }
    }

    fun isInFav(productId: String): Flow<Boolean> =
        context.dataStore.data.map { productId in (it[PrefKeys.FAV_ITEMS] ?: emptySet())}
}