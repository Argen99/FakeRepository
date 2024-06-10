package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.remote.dto.ProductDto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductsDao {

    @Query("SELECT * FROM product")
    fun getAllProducts(): Flow<List<ProductDto>>

    @Query("SELECT * FROM product WHERE category = :category")
    fun getProductsByCategory(category: String): Flow<List<ProductDto>>

    @Query("SELECT * FROM product WHERE id = :id")
    fun getProductById(id: Int): Flow<ProductDto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<ProductDto>)
}