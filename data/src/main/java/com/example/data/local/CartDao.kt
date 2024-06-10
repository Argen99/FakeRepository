package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.remote.dto.CartItem
import com.example.data.remote.dto.ProductDto
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Query("SELECT * FROM cart_item")
    fun getAllCartItems(): Flow<List<CartItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToCart(cartItem: CartItem)

    @Delete
    suspend fun deleteCartItem(cartItem: CartItem)

    @Query("SELECT COUNT(*) FROM cart_item WHERE id = :id")
    suspend fun getProductCountById(id: Int): Int

    @Query("DELETE FROM cart_item")
    suspend fun clearCart()
}