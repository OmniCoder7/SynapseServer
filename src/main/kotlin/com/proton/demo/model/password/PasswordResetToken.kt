package com.proton.demo.model.password

import com.proton.demo.model.user.User
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("Password Reset Token")
class PasswordResetToken (
    val EXPIRATION: Int = 60 * 24,
    val id: Long? = null,
    val token: String? = null,
    val user: User? = null,
    val expiryDate: Date? = null
)