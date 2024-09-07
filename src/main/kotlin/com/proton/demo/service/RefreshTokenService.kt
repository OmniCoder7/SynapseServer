package com.proton.demo.service

import com.proton.demo.model.refresh.RefreshToken
import com.proton.demo.security.JwtService
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*


@Service
class RefreshTokenService(
    private val userAuthService: UserAuthService,
    private val mongoTemplate: MongoTemplate
) {

    fun createRefreshToken(username: String): RefreshToken {
        val user = userAuthService.getUser(username) ?: throw UsernameNotFoundException("User with $username not found")
        val refreshToken = RefreshToken(
            token = UUID.randomUUID().toString(),
            expiryDate = Instant.now().plusMillis(JwtService.ACCESS_TOKEN_VALIDITY),
            userId = user.userId
        )
        return mongoTemplate.save(refreshToken)
    }


    fun findByToken(token: String?) =
        mongoTemplate.findOne(Query.query(Criteria.where("token").`is`(token)), RefreshToken::class.java)

    fun isExpired(token: RefreshToken): Boolean {
        if (token.expiryDate < Instant.now()) {
            mongoTemplate.remove(token)
            return true
        }
        return false
    }
}