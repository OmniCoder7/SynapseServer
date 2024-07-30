package com.proton.demo.service

import com.proton.demo.model.refresh.RefreshToken
import com.proton.demo.respository.RefreshTokenRepository
import com.proton.demo.security.JwtService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*


@Service
class RefreshTokenService(
    private val refreshTokenRepository: RefreshTokenRepository,
    private val userService: UserService
) {

    fun createRefreshToken(username: String): RefreshToken {
        val user = userService.getUser(username) ?: throw UsernameNotFoundException("User with $username not found")
        val refreshToken = RefreshToken(
            token = UUID.randomUUID().toString(),
            expiryDate = Instant.now().plusMillis(JwtService.ACCESS_TOKEN_VALIDITY),
            userId = user.userId
        )
        return refreshTokenRepository.save(refreshToken)
    }


    fun findByToken(token: String?) =
        refreshTokenRepository.findByToken(token!!)

    fun findByUserId(userId: Long) =
         refreshTokenRepository.findByUserId(userId)


    fun isExpired(token: RefreshToken): Boolean {
        if (token.expiryDate < Instant.now()) {
            refreshTokenRepository.delete(token)
            return true
        }
        return false
    }
}