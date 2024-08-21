package com.proton.demo.routes

import com.proton.demo.exception.DuplicateEmailException
import com.proton.demo.model.refresh.JwtResponse
import com.proton.demo.model.refresh.RefreshTokenRequestDTO
import com.proton.demo.model.user.LoginDTO
import com.proton.demo.model.user.LoginResponse
import com.proton.demo.model.user.RegisterUserDTO
import com.proton.demo.model.user.User
import com.proton.demo.security.JwtService
import com.proton.demo.service.AuthenticationService
import com.proton.demo.service.RefreshTokenService
import com.proton.demo.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/auth")
class Controller(
    @Autowired private val userService: UserService,
    private val jwtService: JwtService,
    private val authenticationService: AuthenticationService,
    private val userDetailsService: UserDetailsService,
    private val refreshTokenService: RefreshTokenService,
) {
    @PostMapping("/register")
    fun signUp(@RequestBody registerUserDTO: RegisterUserDTO) =
        try {
            ResponseEntity.ok(authenticationService.signUp(registerUserDTO))
        } catch (e: DuplicateEmailException) {
            ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build()
        }

    @PostMapping("/login")
    fun login(@RequestBody loginDTO: LoginDTO): ResponseEntity<LoginResponse> {
        return try {
            ResponseEntity.ok(authenticationService.authenticate(loginDTO))
        } catch (e: UsernameNotFoundException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
    }

    @GetMapping("/me")
    fun authenticatedUser(@RequestBody token: String): ResponseEntity<LoginResponse> {
        val userDetails = userDetailsService.loadUserByUsername(jwtService.getUsernameFromToken(token))
        val authToken = UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.authorities
        )
        SecurityContextHolder.getContext().authentication = authToken
        val authentication = SecurityContextHolder.getContext().authentication
        val currentAuthUser = authentication.principal as User
        return ResponseEntity.ok(
            LoginResponse(
                accessToken = token,
                refreshToken = refreshTokenService.findByUserId(currentAuthUser.userId)!!.token,
                firstName = currentAuthUser.firstName,
                lastName = currentAuthUser.lastName,
                userId = currentAuthUser.userId,
                email = currentAuthUser.email,
                gender = currentAuthUser.gender,
                dob = currentAuthUser.dob,
                age = currentAuthUser.age,
                userName = currentAuthUser.userName,
                number = currentAuthUser.number,
                address = currentAuthUser.address,
                cardId = currentAuthUser.cardId
            )
        )
    }

    @PostMapping("/refresh")
    fun refreshToken(@RequestBody refreshTokenRequestDTO: RefreshTokenRequestDTO): ResponseEntity<JwtResponse> =
        ResponseEntity.ok(authenticationService.refresh(refreshTokenRequestDTO))


}