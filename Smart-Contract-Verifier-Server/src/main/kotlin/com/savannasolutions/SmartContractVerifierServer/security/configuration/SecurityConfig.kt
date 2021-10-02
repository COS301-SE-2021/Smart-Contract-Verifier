package com.savannasolutions.SmartContractVerifierServer.security.configuration

import io.jsonwebtoken.security.Keys
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Primary
import javax.annotation.PostConstruct
import javax.crypto.SecretKey

@Primary
@ConfigurationProperties("com.unison.security")
data class SecurityConfig(var secretKey: String = "", var timeout: String = "0"){
    lateinit var signingKey: SecretKey

    @PostConstruct
    fun init() {
        signingKey = Keys.hmacShaKeyFor(secretKey.toByteArray())
    }
}