package com.proton.demo.routes

import com.proton.demo.model.user.*
import com.proton.demo.security.JwtService
import com.proton.demo.service.RefreshTokenService
import com.proton.demo.service.UserAuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class Controller(
    private val authenticationManager: AuthenticationManager,
    private val jwtService: JwtService,
    private val refreshTokenService: RefreshTokenService,
    private val userAuthService: UserAuthService
) {
    @PostMapping("/register")
    fun signUp(@RequestBody registerUserDTO: RegisterUserDTO): RegisterResponse {

        val userId = userAuthService.insertUser(registerUserDTO.toUser())!!.userId
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                registerUserDTO.email,
                registerUserDTO.password
            )
        )
        val accessToken = jwtService.generateToken(registerUserDTO.email)
        val refreshToken = refreshTokenService.createRefreshToken(registerUserDTO.email).token
        val res = registerUserDTO.toRegisterResponse(accessToken, refreshToken, userId)

        return res
    }

    @PostMapping("/login")
    fun login(@RequestBody loginDTO: LoginDTO): ResponseEntity<LoginResponse> {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginDTO.email, loginDTO.password)
        )
        SecurityContextHolder.getContext().authentication = authentication
        val user = authentication.principal as User
        val accessToken = jwtService.generateToken(user.email)
        val refreshToken = refreshTokenService.createRefreshToken(user.email)
        return try {
            ResponseEntity.ok(LoginResponse(user = user, accessToken = accessToken, refreshToken = refreshToken.token))
        } catch (e: UsernameNotFoundException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
    }

    @GetMapping("/token")
    fun getCurrentUserByToken(@RequestParam token: String): ResponseEntity<User> {
        return try {
            ResponseEntity.ok(userAuthService.authenticate(token))
        } catch (e: NullPointerException) {
            ResponseEntity.badRequest().build()
        }
    }

    @GetMapping("/id")
    fun getCurrentUserById(@RequestParam id: Long): ResponseEntity<User> {
        return try {
            ResponseEntity.ok(userAuthService.authenticate(id))
        } catch (e: NullPointerException) {
            ResponseEntity.badRequest().build()
        }
    }
}