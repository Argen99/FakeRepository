package com.example.data.core

interface DataMapper<T> {
    fun toDomain(): T
}