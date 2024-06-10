package com.example.fakestore.ui.fragments.products

import com.example.domain.model.Product
import com.example.domain.repository.MainRepository
import com.example.fakestore.core.base.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest

class ProductsViewModel(
    private val repository: MainRepository
): BaseViewModel() {

    private val _serverRequestState = mutableUiStateFlow<Unit>()
    val serverRequestState = _serverRequestState.asStateFlow()

    private val _filterByCategory = MutableStateFlow<String?>(null)
    val filterByCategory = _filterByCategory.asStateFlow()

    private val _categoriesState = mutableUiStateFlow<List<String>>()
    val categoriesState = _categoriesState

    @OptIn(ExperimentalCoroutinesApi::class)
    val products: Flow<List<Product>> = filterByCategory.flatMapLatest { category ->
        repository.getProductsFromRoom(category)
    }

    init {
        fetchAllProductsFromServer()
        getAllCategories()
    }

    private fun fetchAllProductsFromServer() {
        repository.fetchAllProductsFromServer().gatherRequest(_serverRequestState)
    }

    fun filterBy(category: String? = null) {
        if (_filterByCategory.value == category)
            return
        _filterByCategory.value = category

    }

    private fun getAllCategories() {
        repository.getCategories().gatherRequest(_categoriesState)
    }
}