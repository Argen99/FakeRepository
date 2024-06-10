package com.example.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.remote.dto.CartItem
import com.example.data.remote.dto.ProductDto

@Database(entities = [ProductDto::class, CartItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productsDao(): ProductsDao
    abstract fun cartDao(): CartDao
}