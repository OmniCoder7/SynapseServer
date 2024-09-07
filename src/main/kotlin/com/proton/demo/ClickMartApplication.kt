package com.proton.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ClickMartApplication

fun main(args: Array<String>) {
    runApplication<ClickMartApplication>(*args)
}