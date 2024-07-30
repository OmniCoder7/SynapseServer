package com.proton.demo.exception

class DuplicateEmailException(val email: String) : Exception() {
    override val message: String
        get() = "$email is not available for use"
}
