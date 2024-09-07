package com.proton.demo.util


class PasswordValidator {
    companion object {
        private const val PASSWORD_PATTERN =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!?])[A-Za-z\\d@#$%^&+=!?]{8,}$"

        fun isPasswordValid(password: String): Boolean {
            val pattern = PASSWORD_PATTERN.toRegex()
            return pattern.matches(password)
        }

        private fun containsLowercase(password: String): Boolean {
            return password.any { it.isLowerCase() }
        }

        private fun containsUppercase(password: String): Boolean {
            return password.any { it.isUpperCase() }
        }

        private fun containsDigit(password: String): Boolean {
            return password.any { it.isDigit() }
        }

        private fun containsSpecialChar(password: String): Boolean {
            val specialChars = setOf('@', '#', '$', '%', '^', '&', '+', '=', '!', '?')
            return password.any { it in specialChars }
        }
    }
}