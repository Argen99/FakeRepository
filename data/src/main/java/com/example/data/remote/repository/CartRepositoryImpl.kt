package com.example.data.remote.repository

import com.example.data.local.CartDao
import com.example.data.remote.dto.toCartItem
import com.example.domain.model.Product
import com.example.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CartRepositoryImpl(
    private val cartDao: CartDao
): CartRepository {

    override fun getAllCartItems(): Flow<List<Product>> {
        return cartDao.getAllCartItems().map { cartItems ->
            cartItems.map { cartItem -> cartItem.toDomain() }
        }
    }

    override suspend fun insertCartItem(product: Product) {
        cartDao.insertToCart(product.toCartItem())
    }

    override suspend fun deleteCartItem(product: Product) {
        cartDao.deleteCartItem(product.toCartItem())
    }

    override suspend fun itemIsAddedToCart(id: Int): Boolean {
        return cartDao.getProductCountById(id) >= 1
    }

    override suspend fun clearCart() {
        cartDao.clearCart()
    }
}