package com.proton.demo.security

import com.proton.demo.model.OneTimePassword
import com.proton.demo.service.SequenceGeneratorService
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Service
import java.util.*

@Service
class OneTimePasswordService(
    private val oneTimePasswordHelp: OneTimePasswordHelp,
    private val mongoTemplate: MongoTemplate,
    private val sequenceGeneratorService: SequenceGeneratorService
) {
    private val expiryInterval = 5L * 60 * 1000

    fun returnOneTimePassword(userId: Long): OneTimePassword {
        var currentOTP = getOTPByUserId(userId)
        if (currentOTP != null) {
            currentOTP = currentOTP.copy(
                otp = oneTimePasswordHelp.createRandomOneTimePassword().get(),
                expiryDate = Date(System.currentTimeMillis() + expiryInterval)
            )
            mongoTemplate.updateFirst(
                Query(Criteria.where(OneTimePassword::userId.name).`is`(currentOTP.userId)),
                Update().set(OneTimePassword::otp.name, currentOTP.otp)
                    .set(OneTimePassword::expiryDate.name, currentOTP.expiryDate),
                OneTimePassword::class.java
            )
            return currentOTP
        }
        val oneTimePassword = OneTimePassword(
            otp = oneTimePasswordHelp.createRandomOneTimePassword().get(),
            expiryDate = Date(System.currentTimeMillis() + expiryInterval),
            userId = userId,
            id = sequenceGeneratorService.generateSequence(OneTimePassword.OTP_SEQUENCE)
        )
        mongoTemplate.save(oneTimePassword)
        return oneTimePassword
    }

    fun getOTPByUserId(userId: Long) =
        mongoTemplate.findOne(
            Query(Criteria.where(OneTimePassword::userId.name).`is`(userId)),
            OneTimePassword::class.java
        )
}