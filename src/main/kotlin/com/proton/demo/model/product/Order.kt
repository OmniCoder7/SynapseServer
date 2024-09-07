package com.proton.demo.model.product

import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("Orders")
data class Order(
    val orderId: Long,
    val productId: Long,
    val userId: Long,
    val shippingAddress: String,
    val orderDate: Date,
    val quantity: Int
)