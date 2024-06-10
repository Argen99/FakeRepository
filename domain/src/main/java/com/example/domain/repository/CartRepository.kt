package com.example.domain.repository

import com.example.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface CartRepository {

    fun getAllCartItems(): Flow<List<Product>>
    suspend fun insertCartItem(product: Product)
    suspend fun deleteCartItem(product: Product)
    suspend fun itemIsAddedToCart(id: Int): Boolean
    suspend fun clearCart()
}