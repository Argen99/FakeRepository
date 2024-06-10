package com.example.fakestore.ui.fragments.products.product_details

import androidx.lifecycle.viewModelScope
import com.example.domain.model.Product
import com.example.domain.repository.CartRepository
import com.example.domain.repository.MainRepository
import com.example.fakestore.core.base.BaseViewModel
import kotlinx.coroutines.launch

class ProductDetailsViewModel(
    private val mainRepository: MainRepository,
    private val cartRepository: CartRepository
): BaseViewModel() {

    fun getProductById(id: Int) = mainRepository.getProductById(id)

    fun insertCartItem(product: Product) {
        viewModelScope.launch {
            cartRepository.insertCartItem(product)
        }
    }
    suspend fun itemIsAddedToCart(id: Int) = cartRepository.itemIsAddedToCart(id)
}