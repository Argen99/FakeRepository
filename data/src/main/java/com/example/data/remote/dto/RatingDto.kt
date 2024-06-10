package com.example.data.remote.dto

import com.example.data.core.DataMapper
import com.example.domain.model.Rating

data class RatingDto(
    val rate: Double,
    val count: Int
) : DataMapper<Rating> {

    override fun toDomain() = Rating(
        rate = rate,
        count = count
    )
}

fun Rating.toDto() = RatingDto(
    rate = rate,
    count = count
)