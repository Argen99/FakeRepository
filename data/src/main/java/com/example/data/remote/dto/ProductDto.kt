package com.example.data.remote.dto

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.core.DataMapper
import com.example.domain.model.Product

@Entity(tableName = "product")
data class ProductDto(
    @PrimaryKey
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    @Embedded
    val rating: RatingDto
) : DataMapper<Product> {

    override fun toDomain() = Product(
        id = id,
        title = title,
        price = price,
        description = description,
        category = category,
        image = image,
        rating = rating.toDomain()
    )
}

fun Product.toDto() = ProductDto(
    id = id,
    title = title,
    price = price,
    description = description,
    category = category,
    image = image,
    rating = rating.toDto()
)