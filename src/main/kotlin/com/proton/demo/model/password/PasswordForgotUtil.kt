package com.proton.demo.model.password

import com.proton.demo.model.user.User
import com.proton.demo.respository.PasswordResetTokenRepository
import com.proton.demo.security.OneTimePasswordService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.MessageSource
import org.springframework.core.env.Environment
import org.springframework.mail.SimpleMailMessage
import java.util.*


class PasswordForgotUtil(
    private val passwordTokenRepository: PasswordResetTokenRepository,
    private val env: Environment,
    private val messages: MessageSource,
    private val oneTimePasswordService: OneTimePasswordService
) {
    fun createPasswordResetTokenForUser(user: User?, token: String?) {
        val myToken = PasswordResetToken(token = token, user = user)
        passwordTokenRepository.save(myToken)
    }

    fun constructResetTokenEmail(
        locale: Locale, user: User
    ): SimpleMailMessage {
        val otp = oneTimePasswordService.returnOneTimePassword(userId = user.userId)
        val message: String = messages.getMessage(
            "message.resetPassword", null, locale
        )
        return constructEmail("Reset Password", "$message \r\n$otp", user)
    }

    fun constructResendVerificationTokenEmail(
        contextPath: String,
        locale: Locale?,
        token: VerificationToken,
        user: User
    ): SimpleMailMessage {
        val confirmationUrl = contextPath + "/registrationConfirm.html?token=" + token.token
        val message = messages.getMessage("message.resendToken", null, locale!!)
        return constructEmail(
            subject = "Resend Registration Token",
            body = "$message \r\n$confirmationUrl",
            user = user
        )
    }

    private fun constructEmail(
        subject: String, body: String, user: User
    ) = SimpleMailMessage().apply {
        this.subject = subject
        this.text = body
        this.from = env.getProperty("support.email")
        setTo(user.email)
    }

    fun getAppUrl(request: HttpServletRequest): String {
        return "http://" + request.serverName + ":" + request.serverPort + request.contextPath
    }

    fun compareOtp(confirmationOTP: Int, userId: Long): Boolean? {
        val otp = oneTimePasswordService.getOTPByUserId(userId) ?: return null
        return confirmationOTP == otp
    }

    fun validate(otp: Int, newPassword: String, oldPassword: String, userId: Long): Boolean {
        val otpValidity = compareOtp(otp, userId)
        return if (otpValidity != null)
            otpValidity && newPassword == oldPassword
        else false
    }
}