package com.example.data.remote.repository

import com.example.data.core.makeNetworkRequest
import com.example.data.local.ProductsDao
import com.example.data.remote.api_service.MainApiService
import com.example.data.remote.dto.toDto
import com.example.domain.Either
import com.example.domain.model.Product
import com.example.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MainRepositoryImpl(
    private val apiService: MainApiService,
    private val productsDao: ProductsDao
): MainRepository {

    override fun fetchAllProductsFromServer(): Flow<Either<String, Unit>> = makeNetworkRequest {
        apiService.getAllProducts().map { it.toDomain() }.also { products ->
            productsDao.insertAll(products.map { product -> product.toDto() })
        }
    }

    override fun getProductsFromRoom(category: String?): Flow<List<Product>> {
        return if (category == null || category.lowercase() == "all") {
            productsDao.getAllProducts().map { products ->
                products.map { product -> product.toDomain() }
            }
        } else{
            productsDao.getProductsByCategory(category).map { products ->
                products.map { product -> product.toDomain() }
            }
        }
    }

    override fun getProductById(id: Int): Flow<Product> {
        return productsDao.getProductById(id).map { product ->
            product.toDomain()
        }
    }

    override fun getCategories(): Flow<Either<String, List<String>>> = makeNetworkRequest {
        apiService.getAllCategories()
    }
}