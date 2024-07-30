package com.proton.demo.respository

import com.proton.demo.model.user.User
import org.springframework.data.mongodb.repository.MongoRepository

interface AuthRepository : MongoRepository<User, String> {
    fun findByEmail(email: String): User?
    fun findByUserId(id: Long): User?
}