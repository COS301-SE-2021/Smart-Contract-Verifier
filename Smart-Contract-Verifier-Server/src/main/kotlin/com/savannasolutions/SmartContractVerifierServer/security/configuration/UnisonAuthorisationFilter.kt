package com.savannasolutions.SmartContractVerifierServer.security.configuration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class UnisonAuthorisationFilter(var securityConfig: SecurityConfig): OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val uri = request.requestURI
        if(uri == "/user" && request.method == "POST"){
            print("Auth not needed")
            chain.doFilter(request, response)
            return
        }else if(uri.matches(Regex("/user/[a-zA-Z0-9]{42}")) && (request.method == "POST" || request.method == "GET")){
            print("Auth not needed")
            chain.doFilter(request, response)
            return
        }else if(uri.contains("/download") || uri.contains("/linked")){
            print("Auth not needed")
            chain.doFilter(request, response)
            return
        }

        var token = request.getHeader("Authorization")
        if(token!=null){
            val parts = token.split(" ")
            token = parts[1]
            var claimedUser = uri.substringAfter("/user/")
            claimedUser = claimedUser.subSequence(0,42) as String

            //jwt authentication

        }
        response.sendError(403)
        return
    }
}