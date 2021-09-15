package com.savannasolutions.SmartContractVerifierServer.security.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@Configuration
@PropertySource("classpath:application.properties")
class SecurityConfig {
    @Value("com.unison.SecretKey")
    var secretKey: String = ""

    @Value("com.unison.timeout")
    var timeout: String = "0"
}