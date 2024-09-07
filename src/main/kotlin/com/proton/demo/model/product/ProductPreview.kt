package com.proton.demo.model.product

data class ProductPreview(
    val productId: Long,
    val productName: String,
    val price: Int,
    val rating: Double,
    val discount: Int,
    val imageUrl: String? = null,
)