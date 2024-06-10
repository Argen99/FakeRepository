package com.example.domain.repository

import com.example.domain.Either
import com.example.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    fun fetchAllProductsFromServer(): Flow<Either<String, Unit>>
    fun getProductsFromRoom(category: String?): Flow<List<Product>>
    fun getProductById(id: Int): Flow<Product>
    fun getCategories(): Flow<Either<String, List<String>>>
}