package com.proton.demo.model.user

import com.proton.demo.model.address.Address
import org.bson.types.ObjectId

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val id: ObjectId = ObjectId(),
    val userId: Long = 0,
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val gender: String = "",
    val dob: String = "",
    val age: Int = 0,
    var userName: String = "",
    val number: Long = 0,
    val address: List<Address> = listOf(),
    val cardId: List<Int> = listOf()
)
