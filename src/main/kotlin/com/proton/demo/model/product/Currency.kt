package com.proton.demo.model.product

import org.springframework.data.mongodb.core.mapping.Document

@Document("Currency")
data class Currency(
    val id: Long,
    val code: String,
    val symbol: String,
    val name: String
)