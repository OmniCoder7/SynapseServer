package com.proton.demo.model.user

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Document("User")
data class User(
    @Id
    var userId: Long,
    @Field("first_name")
    val firstName: String = "",
    @Field("last_name")
    val lastName: String = "",
    val email: String = "",
    val gender: String = "",
    @Field("date_of_birth")
    val dob: String = "",
    @Field("username")
    var userName: String = "",
    val number: Long = 0,
    @Field("password")
    val loginPassword: String = "",
    var cartProducts: List<Long> = emptyList(),
    var orderIds: List<Long> = emptyList(),
    var wishListIds: List<Long> = emptyList()
) : UserDetails {
    companion object {
        @Transient
        val USER_SEQUENCE: String = "user_sequence"
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableSetOf()
    override fun getPassword(): String = loginPassword
    override fun getUsername(): String = email

}