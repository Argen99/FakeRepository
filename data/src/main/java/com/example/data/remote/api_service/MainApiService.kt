package com.example.data.remote.api_service

import com.example.data.remote.dto.ProductDto
import retrofit2.http.GET

interface MainApiService {

    @GET("products")
    suspend fun getAllProducts(): List<ProductDto>

    @GET("products/categories")
    suspend fun getAllCategories(): List<String>
}