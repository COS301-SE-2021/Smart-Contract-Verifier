package com.savannasolutions.SmartContractVerifierServer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@SpringBootApplication
@EnableWebSecurity
class SmartContractVerifierServerApplication

fun main(args: Array<String>) {
	runApplication<SmartContractVerifierServerApplication>(*args)
}
