package com.proton.demo.model.user

data class RegisterResponse(
    val accessToken: String,
    val refreshToken: String,
    val userId: Long = 0,
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val gender: String = "",
    val dob: String = "",
    val age: Int = 0,
    var userName: String = "",
    val number: Long = 0,
)
