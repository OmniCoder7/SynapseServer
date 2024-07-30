package com.proton.demo.respository

import com.proton.demo.model.refresh.RefreshToken
import org.springframework.data.mongodb.repository.MongoRepository

interface RefreshTokenRepository: MongoRepository<RefreshToken, Long> {
    fun findByToken(token: String): RefreshToken?
    fun findByUserId(userId: Long): RefreshToken?
}