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
        val responseHeader = HttpHeaders()
        responseHeader.set(HttpHeaders.AUTHORIZATION, jwtService.generateToken(registerUserDTO.email))
        responseHeader.set("X-Refresh-Token", refreshTokenService.createRefreshToken(registerUserDTO.email).token)
        val res = registerUserDTO.toRegisterResponse(user.userId)

        return ResponseEntity.ok()
            .headers(responseHeader)
            .body(res)
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
        val responseHeader = HttpHeaders()
        responseHeader.set(HttpHeaders.AUTHORIZATION, jwtService.generateToken(loginDTO.email))
        responseHeader.set("X-Refresh-Token", refreshTokenService.createRefreshToken(loginDTO.email).token)
        return ResponseEntity.ok()
            .headers(responseHeader)
            .body(LoginResponse(user))
    }

    @GetMapping("/token")
    fun getCurrentUserByToken(
        @RequestHeader(HttpHeaders.AUTHORIZATION) accessToken: String,
        @RequestHeader("X-Refresh-Token") refreshToken: String
    ): ResponseEntity<User> {
        val token = accessToken.substringAfter("Bearer ")
        return try {
            ResponseEntity.ok(userAuthService.authenticate(token))
        } catch (_: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        } catch (_: UserNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        } catch (_: TokenExpiredException) {
            if (refreshTokenService.isExpired(refreshToken))
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            else {
                val user = userAuthService.getUser(jwtService.getUsernameFromToken(token))
                    ?: return ResponseEntity.badRequest().build()
                val newRefreshToken = refreshTokenService.createRefreshToken(user.email)
                val newAccessToken = jwtService.generateToken(user.email)
                val responseHeader = HttpHeaders()
                responseHeader.set(HttpHeaders.AUTHORIZATION, newAccessToken)
                responseHeader.set("X-Refresh-Token", newRefreshToken.token)
                ResponseEntity.ok().headers(responseHeader)
                    .body(userAuthService.authenticate(newRefreshToken.userId))
            }
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