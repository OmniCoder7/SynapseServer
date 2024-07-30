package com.proton.demo.model.product

import org.springframework.data.mongodb.core.mapping.Document

@Document("Brand")
data class Brand(
    val id: Long,
    val name: String,
    val description: String? = null,
    val logoImageId: String? = null
)