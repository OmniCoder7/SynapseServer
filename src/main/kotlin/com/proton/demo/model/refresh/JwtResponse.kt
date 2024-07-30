package com.proton.demo.model.refresh

data class JwtResponse(
    val accessToken: String,
    val refreshToken: String
)