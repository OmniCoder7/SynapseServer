package com.proton.demo.model.refresh

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.Instant

@Document("Refresh Token")
data class RefreshToken(
    @Field("_id")
    val id: ObjectId = ObjectId(),
    val token: String,
    var expiryDate: Instant,
    val userId: Long
)
