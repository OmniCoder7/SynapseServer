package com.proton.demo.model.password

import java.util.*


data class VerificationToken(
    val id: Long? = null,
    var token: String,
    val userId: Long,
    var expiryDate: Date
) {

    companion object {
        private const val EXPIRATION = 60 * 24

        fun calculateExpiryDate(expiryTimeInMinutes: Int): Date {
            val cal = Calendar.getInstance()
            cal.time = Date()
            cal.add(Calendar.MINUTE, expiryTimeInMinutes)
            return cal.time
        }
    }

    fun updateToken(newToken: String) {
        this.token = newToken
        this.expiryDate = calculateExpiryDate(EXPIRATION)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is VerificationToken) return false

        if (expiryDate != other.expiryDate) return false
        if (token != other.token) return false
        if (userId != other.userId) return false
        return true
    }

    override fun hashCode(): Int {
        var result = token.hashCode()
        result = 31 * result + userId.hashCode()
        result = 31 * result + (expiryDate?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "VerificationToken(token='$token', user=$userId, expiryDate=$expiryDate)"
    }
}