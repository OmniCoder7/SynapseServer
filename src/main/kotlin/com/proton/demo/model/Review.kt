package com.proton.demo.model

import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "Reviews")
data class Review(
    val id: String,
    val userId: String,
    val rating: Int,
    val comment: String,
    val timestamp: LocalDateTime = LocalDateTime.now()
)