package com.savannasolutions.SmartContractVerifierServer.security.configuration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.server.ServerWebExchange
import java.util.*

@Configuration
@Import(SecurityConfig::class)
class UnisonWebSecurityConfigurer: WebSecurityConfigurerAdapter() {
    @Autowired
    lateinit var securityConfig: SecurityConfig

    override fun configure(http: HttpSecurity?) {
        http?.cors()?.and()?.csrf()?.disable()?.sessionManagement()?.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            ?.and()?.addFilterAfter(UnisonAuthorisationFilter(securityConfig), BasicAuthenticationFilter::class.java)
    }

}