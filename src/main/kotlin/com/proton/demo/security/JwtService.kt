package com.proton.demo.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtService {
    companion object {
        val secret: SecretKey =
            Keys.hmacShaKeyFor(Decoders.BASE64.decode("sdfghjkuytrdxcvbnjkiuytrdfvbnjkiuytfcvbnjuytgfvbnjuhygfghfddfg"))
        const val ACCESS_TOKEN_VALIDITY: Long = 24 * 60 * 60 * 1000
        const val REFRESH_TOKEN_VALIDITY: Long = (10 * 24 * 60 * 60 * 1000).toLong()
    }

    fun getExpirationDateFromToken(token: String) =
        getClaimsFromToken(token, Claims::getExpiration)

    fun getUsernameFromToken(token: String): String =
        getClaimsFromToken(token, Claims::getSubject)
            ?: throw IllegalArgumentException("Token does not contain a subject claim")

    fun <T> getClaimsFromToken(token: String, claimsResolver: (Claims) -> T): T {
        val claims = getAllClaimsFromToken(token)
        return claimsResolver.invoke(claims)
    }

    fun getAllClaimsFromToken(token: String) =
        Jwts.parser().verifyWith(secret).build().parseSignedClaims(token).payload

    fun isTokenExpired(token: String) =
        getExpirationDateFromToken(token).before(Date())

    fun generateToken(username: String): String {
        val claims: Map<String, Any> = HashMap()
        return doGenerateToken(claims, username)
    }

    private fun doGenerateToken(claims: Map<String, Any?>, subject: String): String {
        return Jwts.builder().claims(claims).subject(subject).issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
            .signWith(secret).compact()
    }

    fun validateToken(token: String?, userDetails: UserDetails): Boolean {
        val username = getUsernameFromToken(token!!)
        return (username == userDetails.username && !isTokenExpired(token))
    }
}