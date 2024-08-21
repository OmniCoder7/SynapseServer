package com.proton.demo.respository

import com.proton.demo.model.OneTimePassword
import org.springframework.data.mongodb.repository.MongoRepository

interface OneTimePasswordRepository: MongoRepository<OneTimePassword, Long> {
    fun findByUserId(userId: Long): OneTimePassword?
}