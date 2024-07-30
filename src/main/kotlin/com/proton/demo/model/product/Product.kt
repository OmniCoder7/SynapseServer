package com.proton.demo.model.product

import org.bson.codecs.pojo.annotations.BsonId
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

data class Product(
    @BsonId
    val id: Long,
    val name: String,
    val description: String,
    val categoryId: String,
    val brandId: String,
    val price: Double,
    val discountedPrice: Double? = null,
    val currencyId: String,
    val stockQuantity: Int,
    val isOnSale: Boolean,
    val saleStartDate: LocalDateTime? = null,
    val saleEndDate: LocalDateTime? = null,
    val imageUrls: List<String>,
    val videoUrls: List<String>? = null,
    val attributes: List<Attribute>,
    val options: List<Option>? = null,
    val variants: List<Variant>? = null,
    val reviews: List<Review>? = null,
    val rating: Double? = null,
    val createdAt: Date = Date.from(Instant.now()),
    val updatedAt: Date? = null
) {
    val isInStock: Boolean = stockQuantity > 0
}

data class Attribute(
    val name: String,
    val value: String
)

data class Option(
    val name: String,
    val values: List<OptionValue>
)

data class OptionValue(
    val value: String
)

data class Variant(
    val price: Double? = null,
    val discountedPrice: Double? = null,
    val stockQuantity: Int
) {
    val isInStock: Boolean = stockQuantity > 0
}

data class Review(
    val userId: Long,
    val rating: Int,
    val title: String? = null,
    val content: String,
    val createdAt: Date
)