package com.proton.demo.util

import java.util.regex.Pattern

class EmailValidator {
    companion object {

        private val EMAIL_ADDRESS = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )

        fun isEmailValid(email: String): Boolean {
            val pattern = EMAIL_ADDRESS.toRegex()
            return !(pattern.matches(email))
        }

    }
}