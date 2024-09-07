package com.proton.demo.model.user

data class LoginDTO(
    val email: String,
    val password: String
) {
    fun toJwtRequest() =
        LoginRequest(email, password)
}
