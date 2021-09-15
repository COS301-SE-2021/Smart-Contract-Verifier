package com.savannasolutions.SmartContractVerifierServer.security.configuration

import io.jsonwebtoken.SigningKeyResolver
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.PropertySource
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