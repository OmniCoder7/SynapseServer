package com.proton.demo.model.user

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
    val cartId: List<Long> = listOf(),
    val orderIds: List<Long> = listOf(),
    val wishListIds: List<Long> = listOf(),
) {
    constructor(accessToken: String, refreshToken: String, user: User) : this(
        accessToken = accessToken,
        refreshToken = refreshToken,
        userId = user.userId,
        firstName = user.firstName,
        lastName = user.lastName,
        email = user.email,
        gender = user.gender,
        dob = user.dob,
        userName = user.userName,
        number = user.number,
        cartId = user.cartProducts,
        orderIds = user.orderIds,
        wishListIds = user.wishListIds,
    )
}
