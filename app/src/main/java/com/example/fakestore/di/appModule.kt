package com.example.fakestore.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import com.example.fakestore.ui.fragments.products.ProductsViewModel
import com.example.fakestore.ui.fragments.products.product_details.ProductDetailsViewModel
import com.example.fakestore.ui.fragments.cart.CartViewModel
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::ProductsViewModel)
    viewModelOf(::ProductDetailsViewModel)
    viewModelOf(::CartViewModel)
}