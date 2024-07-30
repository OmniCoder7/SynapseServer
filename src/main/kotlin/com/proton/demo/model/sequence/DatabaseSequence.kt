package com.proton.demo.model.sequence

import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "database_sequences")
data class DatabaseSequence(
    val id: String,
    val seq: Long
)