package com.proton.demo.model.user

import jakarta.validation.constraints.NotEmpty


data class LoginDTO(
    @NotEmpty(message = "Email Address is required")
    val email: String,
    @NotEmpty(message = "Password is required")
    val password: String
) {
    fun toJwtRequest() =
        LoginRequest(email, password)
}
