package com.proton.demo.config

import com.proton.demo.service.UserAuthService
import kotlinx.coroutines.runBlocking
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder


@Configuration
class Config(
    private val service: UserAuthService,
) {
    @Bean
    fun userDetailsService(): UserDetailsService =
        UserDetailsService { username ->
            val user: com.proton.demo.model.user.User
            runBlocking {
                user =
                    service.getUser(email = username) ?: throw UsernameNotFoundException("Email address is Invalid")
            }
            user
        }

    @Bean
    fun getPasswordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager =
        config.authenticationManager ?: throw Exception()

    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()

        authProvider.setUserDetailsService(userDetailsService())
        authProvider.setPasswordEncoder(getPasswordEncoder())

        return authProvider
    }
}