package com.proton.demo.service

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import com.proton.demo.exception.DuplicateEmailException
import com.proton.demo.model.user.User
import com.proton.demo.mongoDatabase
import com.proton.demo.respository.AuthRepository
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service


@Service
class UserService(
    private val authRepository: AuthRepository,
    private val sequenceGeneratorService: SequenceGeneratorService,
) {

    val userCollection = mongoDatabase().getCollection<User>("User")

    fun getUser(email: String): User? {
        val user: User
        runBlocking {
            user = userCollection.find(Filters.and(listOf(eq(User::email.name, email)))).first()
        }
        return user
    }

    fun insertUser(user: User): User? {
        runBlocking {
            if (userCollection.find(Filters.and(listOf(eq(User::email.name, user.email)))).count() != 0)
                throw DuplicateEmailException(user.email)
            userCollection.insertOne(
                user.copy(
                    loginPassword = BCryptPasswordEncoder().encode(user.loginPassword),
                    userId = sequenceGeneratorService.generateSequence(User.USER_SEQUENCE)
                )
            )
        }
        return user
    }

    fun getUser(userId: Long) =
        authRepository.findByUserId(userId)

    fun updatePassword(password: String, loginPassword: String) {
        val query = eq(User::loginPassword.name, loginPassword)
        val updates = Updates.min(User::loginPassword.name, password)
        val options = UpdateOptions().upsert(true)
        runBlocking { userCollection.updateOne(query, updates, options) }
    }
}