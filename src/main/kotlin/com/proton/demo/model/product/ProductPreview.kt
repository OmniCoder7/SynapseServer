package com.proton.demo.model.product

import org.bson.codecs.pojo.annotations.BsonId

data class ProductPreview(
    @BsonId val id: Int? = null,
    val name: String,
    val price: Double,
    val discountedPrice: Double,
    val rating: Double,
    val isOnSale: Boolean,
    val imageUrl: String
)