package com.proton.demo.respository

import com.proton.demo.model.password.PasswordResetToken
import org.springframework.data.mongodb.repository.MongoRepository

interface PasswordResetTokenRepository : MongoRepository<PasswordResetToken, String> {
    fun findByToken(token: String?): PasswordResetToken
    fun findByOTP()
}