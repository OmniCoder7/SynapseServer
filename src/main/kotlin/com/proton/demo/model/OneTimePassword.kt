package com.proton.demo.model

import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("OTP")
data class OneTimePassword(
    var id: Long = 0,
    val userId: Long,
    val otp: Int,
    val expiryDate: Date
)
