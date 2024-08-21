package com.proton.demo.security

import com.proton.demo.model.OneTimePassword
import com.proton.demo.respository.OneTimePasswordRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class OneTimePasswordService(
    private val oneTimePasswordRepository: OneTimePasswordRepository,
    private val oneTimePasswordHelp: OneTimePasswordHelp
) {
    private val expiryInterval = 5L * 60 * 1000 // 5 minutes

    fun returnOneTimePassword(userId: Long): OneTimePassword {
        val oneTimePassword = OneTimePassword(
            otp = oneTimePasswordHelp.createRandomOneTimePassword().get(),
            expiryDate = Date(System.currentTimeMillis() + expiryInterval),
            userId = userId
        )
        oneTimePasswordRepository.save(oneTimePassword)
        return oneTimePassword
    }

    fun getOTPByUserId(userId: Long) =
        try {
            oneTimePasswordRepository.findByUserId(userId)!!.otp
        } catch (e: NullPointerException) {
            null
        }

}