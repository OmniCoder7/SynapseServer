package com.proton.demo.model.user

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty
import org.bson.types.ObjectId

data class RegisterUserDTO(
    val id: ObjectId = ObjectId(),
    val userId: Long = 0,
    @NotEmpty(message = "First name is required")
    val firstName: String = "",
    val lastName: String = "",
    @NotEmpty(message = "Email is required")
    val email: String = "",
    @NotEmpty(message = "Gender is required")
    val gender: String = "",
    val dob: String = "",
    val age: Int = 0,
    @NotEmpty(message = "Username is required")
    val username: String = "",
    @Min(message = "First name is required", value = 1000000000)
    val number: Long = 0,
    @NotEmpty(message = "Password is required")
    val password: String = "", // Bcrypt this password to store in database
    val address: List<Address> = listOf(),
    val cardId: List<Int> = listOf()
) {
    fun toUser() =
        User(
            userId = this.userId,
            firstName = this.firstName,
            lastName = this.lastName,
            email = this.email,
            gender = this.gender,
            dob = this.dob,
            age = this.age,
            userName = this.username,
            number = this.number,
            loginPassword = this.password,
        )

    fun toRegisterResponse(accessToken: String, refreshToken: String, id: Long) =
        RegisterResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            userId = id,
            firstName = firstName,
            lastName, email, gender, dob, age,
            username, number
        )
}