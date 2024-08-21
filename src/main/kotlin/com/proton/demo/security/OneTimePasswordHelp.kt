package com.proton.demo.security

import io.jsonwebtoken.lang.Supplier
import kotlin.random.Random

object OneTimePasswordHelp {
    private const val LENGTH = 6

    fun createRandomOneTimePassword(): Supplier<Int> {
        return Supplier<Int> {
            val random: Random = Random.Default
            val oneTimePassword = StringBuilder()
            for (i in 0 until LENGTH) {
                val randomNumber: Int = random.nextInt(10)
                oneTimePassword.append(randomNumber)
            }
            oneTimePassword.toString().trim { it <= ' ' }.toInt()
        }
    }
}