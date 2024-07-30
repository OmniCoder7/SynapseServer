package com.proton.demo.model.password

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("password_reset_token")
data class PasswordResetToken(
    @Id
    val token: String,
    val userId: Long,
    val expiryDate: Date
){
    companion object {
        const val EXPIRATION = 60 * 24
    }
}
