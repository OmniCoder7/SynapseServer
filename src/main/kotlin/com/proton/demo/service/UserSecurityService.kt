package com.proton.demo.service

import com.proton.demo.model.password.PasswordResetToken
import com.proton.demo.respository.PasswordResetTokenRepository
import java.util.*


class UserSecurityService(
    private val passwordTokenRepository: PasswordResetTokenRepository
) {
    fun validatePasswordResetToken(token: String?): String? {
        val passToken = passwordTokenRepository.findByToken(token)
        return if (!isTokenFound(passToken)) "invalidToken"
        else (if (isTokenExpired(passToken)) "expired"
        else null)
    }

    private fun isTokenFound(passToken: PasswordResetToken?): Boolean {
        return passToken != null
    }

    private fun isTokenExpired(passToken: PasswordResetToken?): Boolean {
        val cal = Calendar.getInstance()
        return passToken?.expiryDate?.before(cal.time)!!
    }
}