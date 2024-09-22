package com.proton.demo.service

import com.proton.demo.model.refresh.JwtResponse
import com.proton.demo.model.refresh.RefreshTokenRequestDTO
import com.proton.demo.security.JwtService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    @Autowired private val userAuthService: UserAuthService,
    @Autowired private val refreshTokenService: RefreshTokenService,
    @Autowired private val jwtService: JwtService
) {
    fun refresh(refreshTokenRequestDTO: RefreshTokenRequestDTO): JwtResponse {
        val refreshToken = refreshTokenService.findByToken(refreshTokenRequestDTO.token)
            ?: throw RuntimeException("Refresh token not in database")
        val user = userAuthService.getUser(refreshToken.userId)
        if (refreshTokenService.isExpired(refreshToken.token))
            refreshTokenService.createRefreshToken(user!!.username)
        return JwtResponse(accessToken = jwtService.generateToken(user!!.username), refreshToken = refreshToken.token)
    }
}