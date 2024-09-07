package com.proton.demo.model.password

import com.proton.demo.security.OneTimePasswordService
import org.springframework.stereotype.Component

@Component
class PasswordForgotUtil(
    private val oneTimePasswordService: OneTimePasswordService
) {

    fun compareOtp(confirmationOTP: Int, userId: Long): Boolean? {
        val otp = oneTimePasswordService.getOTPByUserId(userId) ?: return null
        return confirmationOTP == otp.otp
    }

    fun validate(otp: Int, newPassword: String, oldPassword: String, userId: Long): Boolean {
        val otpValidity = compareOtp(otp, userId)
        return if (otpValidity != null)
            otpValidity && newPassword == oldPassword
        else false
    }
}