package com.proton.demo.respository
import com.proton.demo.model.password.VerificationToken
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*
import java.util.stream.Stream

interface VerificationTokenRepository : MongoRepository<VerificationToken, Long?> {
    fun findByToken(token: String): VerificationToken
    fun findByUserId(userId: Long): VerificationToken?
    fun findAllByExpiryDateLessThan(now: Date?): Stream<VerificationToken?>?
    fun deleteByExpiryDateLessThan(now: Date?)
}