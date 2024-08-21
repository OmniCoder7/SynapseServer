package com.proton.demo.model

data class PasswordDto(
    val newPassword: String,
    val otp: Int,
    val userId: Long
)