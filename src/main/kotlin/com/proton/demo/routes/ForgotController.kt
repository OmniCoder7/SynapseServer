package com.proton.demo.routes

import com.proton.demo.exception.UserNotFoundException
import com.proton.demo.model.PasswordDto
import com.proton.demo.model.password.PasswordForgotUtil
import com.proton.demo.model.user.User
import com.proton.demo.security.OneTimePasswordService
import com.proton.demo.service.EmailService
import com.proton.demo.service.UserAuthService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.mail.MailAuthenticationException
import org.springframework.mail.MailParseException
import org.springframework.mail.MailSendException
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/forgetPassword")
class ForgotController(
    private val forgotUtil: PasswordForgotUtil,
    private val messages: MessageSource,
    private val emailService: EmailService,
    private val userAuthService: UserAuthService,
    private val oneTimePasswordService: OneTimePasswordService
) {
    @PostMapping("/resetPassword")
    fun resetPassword(
        request: HttpServletRequest, @RequestParam("email") userEmail: String
    ): ResponseEntity<String> {
        kotlin.runCatching {
            val user: User = userAuthService.getUser(userEmail) ?: throw UserNotFoundException(userEmail)
            emailService.sendMail(
                subject = "Reset Password",
                to = userEmail,
                content = "OTP for resetting the password is \n ${oneTimePasswordService.returnOneTimePassword(user.userId)}"
            )
        }.getOrElse { exception ->
            when (exception) {
                is MailParseException -> return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(("The provided email message does not adhere to the expected format standards."))

                is MailAuthenticationException -> return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(("The credentials provided for email authentication are incorrect or insufficient."))

                is MailSendException -> return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("The email could not be delivered due to an unexpected error."))

                is UserNotFoundException -> println("Bhai nhi mil rha")

                else -> throw exception
            }
        }
        return ResponseEntity.ok("Done")
    }

    @PostMapping("/savePassword")
    fun savePassword(@RequestBody passwordDto: PasswordDto): ResponseEntity<Boolean> {
        val user: User = userAuthService.getUser(passwordDto.userId) ?: throw UserNotFoundException(passwordDto.userId)
        val validity = forgotUtil.validate(passwordDto.otp, passwordDto.newPassword, user.loginPassword, user.userId)
        if (validity) {
            userAuthService.updatePassword(passwordDto.newPassword, user.loginPassword)
            return ResponseEntity.ok(true)
        } else {
            return ResponseEntity.badRequest().body(false)
        }
    }
}