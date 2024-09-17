package com.proton.demo.exception

class TokenExpiredException(message: String) : Exception(message) {
    constructor() : this("Token has expired")

    override val message: String?
        get() = message
}