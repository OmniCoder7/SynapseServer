package com.proton.demo.exception

class UserNotFoundException : RuntimeException {
    val email: String?
    val id: Long?
    constructor(email: String) : super("User not found with ID: $email") {
        this.email = email
        this.id = null
    }
    constructor(id: Long): super("User with user ID $id not found") {
        this.email = null
        this.id = id
    }
    override fun toString(): String {
        return "UserNotFoundException(userId='$email', message='${message}')"
    }
    fun getDetails(): String {
        return "Additional details: User with ID '$email' could not be found in the database or cache."
    }
    companion object {
        private const val serialVersionUID: Long = 1L
    }
}