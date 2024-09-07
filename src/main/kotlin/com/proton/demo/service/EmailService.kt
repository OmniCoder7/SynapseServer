package com.proton.demo.service

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailService(
    private val javaMailSender: JavaMailSender
) {
    fun sendMail(to: String, subject: String, content: String) {
        val simpleMailMessage = SimpleMailMessage().apply {
            setTo(to)
            this.subject = subject
            this.text = content
            this.from = "rishabhsaraswat17@gmail.com"
        }
        javaMailSender.send(simpleMailMessage)
    }
}