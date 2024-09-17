package com.proton.demo.routes

import com.proton.demo.exception.TokenExpiredException
import com.proton.demo.exception.UserNotFoundException
import com.proton.demo.model.user.*
import com.proton.demo.security.JwtService
import com.proton.demo.service.RefreshTokenService
import com.proton.demo.service.UserAuthService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
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
    fun signUp(@RequestBody registerUserDTO: RegisterUserDTO): ResponseEntity<RegisterResponse> {
        val user = userAuthService.insertUser(registerUserDTO.toUser()) ?: return ResponseEntity.badRequest().build()
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                registerUserDTO.email,
                registerUserDTO.password
            )
        )
        val accessToken = jwtService.generateToken(registerUserDTO.email)
        val refreshToken = refreshTokenService.createRefreshToken(registerUserDTO.email).token
        val res = registerUserDTO.toRegisterResponse(accessToken, refreshToken, user.userId)

        return ResponseEntity.ok(res)
    }

    @PostMapping("/login")
    fun login(@RequestBody loginDTO: LoginDTO): ResponseEntity<LoginResponse> {
        val loggingUser = userAuthService.getUser(loginDTO.email)
        if (loggingUser == null || loggingUser.password == loginDTO.password) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginDTO.email, loginDTO.password)
        )
        SecurityContextHolder.getContext().authentication = authentication
        val user = authentication.principal as User
        val accessToken = jwtService.generateToken(user.email)
        val refreshToken = refreshTokenService.createRefreshToken(user.email)
        return ResponseEntity.ok(
            LoginResponse(
                user = user,
                accessToken = accessToken,
                refreshToken = refreshToken.token
            )
        )
    }

    @GetMapping("/token")
    fun getCurrentUserByToken(@RequestHeader(HttpHeaders.AUTHORIZATION) token: String): ResponseEntity<User> {
        val token = token.substringAfter("Bearer ")
        println("Extracted Token: $token")
        return try {
            ResponseEntity.ok(userAuthService.authenticate(token))
        } catch (_: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        } catch (_: UserNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        } catch (_: TokenExpiredException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
    }

    @GetMapping("/{id}")
    fun getCurrentUserById(@PathVariable id: Long): ResponseEntity<User> {
        val loggingUser = userAuthService.authenticate(id)
        if (loggingUser == null) {
            return ResponseEntity.badRequest().build()
        }
        return ResponseEntity.ok(loggingUser)
    }
}