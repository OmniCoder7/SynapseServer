package com.proton.demo.service

import com.proton.demo.exception.DuplicateEmailException
import com.proton.demo.model.user.User
import com.proton.demo.security.JwtService
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserAuthService(
    private val sequenceGeneratorService: SequenceGeneratorService,
    private val mongoTemplate: MongoTemplate,
    private val jwtService: JwtService
) {
    fun getUser(email: String) =
        mongoTemplate.findOne(Query(Criteria.where(User::email.name).`is`(email)), User::class.java)

    fun insertUser(user: User): User? {
        val savedUser: User
        println(user.email)
        val existingUser = getUser(user.email)
        if (existingUser != null)
            throw DuplicateEmailException(user.email)
        else {
            savedUser = user.copy(
                userId = sequenceGeneratorService.generateSequence(User.USER_SEQUENCE),
                loginPassword = BCryptPasswordEncoder().encode(user.password)
            )
            mongoTemplate.save(savedUser)
        }
        return savedUser
    }

    fun getUser(userId: Long) =
        mongoTemplate.findOne(Query(Criteria.where(User::userId.name).`is`(userId)), User::class.java)

    fun updatePassword(password: String, currentPassword: String) {
        mongoTemplate.updateFirst(
            Query(Criteria.where(User::loginPassword.name).isEqualTo((currentPassword))),
            Update().set(User::loginPassword.name, BCryptPasswordEncoder().encode(currentPassword)),
            User::class.java
        )
    }

    fun authenticate(token: String): User? {
        val username = jwtService.getUsernameFromToken(token)
        return mongoTemplate.findOne(Query(Criteria.where(User::email.name).`is`(username)), User::class.java)
    }

    fun authenticate(id: Long): User? {
        val username = getUser(id)
        return mongoTemplate.findOne(Query(Criteria.where(User::email.name).`is`(username)), User::class.java)
    }
}