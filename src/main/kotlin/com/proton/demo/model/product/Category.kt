package com.proton.demo.model.product

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document("Category")
data class Category(
    val id: Long,
    val name: String,
    @Field("sub_category")
    val subCategory: List<String>
)