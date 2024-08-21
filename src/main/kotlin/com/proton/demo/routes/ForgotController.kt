package com.proton.demo.routes

import com.proton.demo.exception.UserNotFoundException
import com.proton.demo.model.PasswordDto
import com.proton.demo.model.password.PasswordForgotUtil
import com.proton.demo.model.password.Result
import com.proton.demo.model.user.User
import com.proton.demo.service.UserSecurityService
import com.proton.demo.service.UserService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.MessageSource
import org.springframework.context.NoSuchMessageException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.mail.MailAuthenticationException
import org.springframework.mail.MailParseException
import org.springframework.mail.MailSendException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/forgetPassword")
class ForgotController(
    private val mailSender: JavaMailSender,
    private val forgotUtil: PasswordForgotUtil,
    private val messages: MessageSource,
    private val userSecurityService: UserSecurityService,
    private val userService: UserService
) {
    @PostMapping("/resetPassword")
    fun resetPassword(
        request: HttpServletRequest,
        @RequestParam("email") userEmail: String?
    ): ResponseEntity<Result> {
        if (userEmail == null) {
            return ResponseEntity.badRequest().build()
        }
        val user: User = userService.getUser(userEmail) ?: throw UserNotFoundException(userEmail)
        kotlin.runCatching {
            mailSender.send(
                forgotUtil.constructResetTokenEmail(
                    request.locale,
                    user
                )
            )
        }.getOrElse { exception ->
            return when (exception) {
                is MailParseException -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Result("The provided email message does not adhere to the expected format standards."))

                is MailAuthenticationException -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Result("The credentials provided for email authentication are incorrect or insufficient."))

                is MailSendException -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Result("The email could not be delivered due to an unexpected error."))

                else -> throw exception
            }
        }
        val result = runCatching {
            Result(
                message = messages.getMessage(
                    "message.resetPasswordEmail", null,
                    request.locale
                )
            )
        }.getOrElse { exception ->
            when (exception) {
                is NoSuchMessageException -> {
                    println("Some problem in messages.properties")
                    Result(message = "Default reset password message")
                }

                else -> throw exception
            }
        }
        return ResponseEntity.ok(result)
    }

    @PostMapping("/savePassword")
    fun savePassword(locale: Locale?, passwordDto: PasswordDto): ResponseEntity<Boolean> {
        val user: User = userService.getUser(passwordDto.userId) ?: throw UserNotFoundException(passwordDto.userId)
        val validity = forgotUtil.validate(passwordDto.otp, passwordDto.newPassword, user.loginPassword, user.userId)
        if (validity) {
            userService.updatePassword(passwordDto.newPassword, user.loginPassword)
            return ResponseEntity.ok(true)
        } else {
            return ResponseEntity.badRequest().body(false)
        }
    }
}