package com.example.fakestore.ui.fragments.cart

import androidx.lifecycle.viewModelScope
import com.example.domain.model.Product
import com.example.domain.repository.CartRepository
import com.example.fakestore.core.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartViewModel(
    private val cartRepository: CartRepository
): BaseViewModel() {

    private val _cartState = MutableStateFlow<List<Product>>(emptyList())
    val cartState = _cartState.asStateFlow()

    init {
        getCartItems()
    }

    private fun getCartItems() {
        viewModelScope.launch(Dispatchers.IO) {
            cartRepository.getAllCartItems().collect {
                _cartState.value = it
            }
        }
    }

    fun deleteCartItem(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            cartRepository.deleteCartItem(product)
            getCartItems()
        }
    }

    fun clearCart() {
        viewModelScope.launch(Dispatchers.IO) {
            cartRepository.clearCart()
            getCartItems()
        }
    }
}