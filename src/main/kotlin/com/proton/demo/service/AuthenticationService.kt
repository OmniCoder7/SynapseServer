package com.proton.demo.service

import com.proton.demo.model.refresh.JwtResponse
import com.proton.demo.model.refresh.RefreshTokenRequestDTO
import com.proton.demo.model.user.LoginDTO
import com.proton.demo.model.user.LoginResponse
import com.proton.demo.model.user.RegisterResponse
import com.proton.demo.model.user.RegisterUserDTO
import com.proton.demo.security.JwtService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    @Autowired private val userService: UserService,
    @Autowired private val refreshTokenService: RefreshTokenService,
    @Autowired private val authenticationManager: AuthenticationManager,
    @Autowired private val jwtService: JwtService
) {

    fun signUp(registerUserDTO: RegisterUserDTO): RegisterResponse {
        val userId = userService.insertUser(registerUserDTO.toUser())!!.userId
        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(registerUserDTO.email, registerUserDTO.password))
        val accessToken = jwtService.generateToken(registerUserDTO.email)
        val refreshToken = refreshTokenService.createRefreshToken(registerUserDTO.email).token
        return registerUserDTO.toRegisterResponse(accessToken, refreshToken, userId)
    }

    fun authenticate(loginDTO: LoginDTO): LoginResponse {
        val authentication =
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(loginDTO.email, loginDTO.password))
        val user = userService.getUser(loginDTO.email) ?: throw UsernameNotFoundException("")
        if (authentication.isAuthenticated) {
            val refreshToken = refreshTokenService.createRefreshToken(loginDTO.email)
            return LoginResponse(
                accessToken = jwtService.generateToken(loginDTO.email),
                refreshToken = refreshToken.token,
                userId = user.userId,
                firstName = user.firstName,
                lastName = user.lastName,
                email = user.email,
                gender = user.gender,
                dob = user.dob,
                age = user.age,
                userName = user.userName,
                number = user.number,
                address = user.address,
                cardId = user.cardId
            )
        } else {
            throw UsernameNotFoundException("Invalid User Request")
        }
    }

    fun refresh(refreshTokenRequestDTO: RefreshTokenRequestDTO): JwtResponse {
        val refreshToken = refreshTokenService.findByToken(refreshTokenRequestDTO.token)
            ?: throw RuntimeException("Refresh token not in database")
        val user = userService.getUser(refreshToken.userId)
        if (refreshTokenService.isExpired(refreshToken))
            refreshTokenService.createRefreshToken(user!!.username)
        return JwtResponse(accessToken = jwtService.generateToken(user!!.username), refreshToken = refreshToken.token)
    }
}