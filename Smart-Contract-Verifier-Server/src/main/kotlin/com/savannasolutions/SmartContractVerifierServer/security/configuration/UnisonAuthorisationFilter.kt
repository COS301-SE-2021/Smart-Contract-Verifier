package com.savannasolutions.SmartContractVerifierServer.security.configuration

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
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
            chain.doFilter(request, response)
            return
        }else if((uri.matches(Regex("/user/[a-zA-Z0-9]{42}"))) && (request.method == "POST" || request.method == "GET")){
            chain.doFilter(request, response)
            return
        }else if(uri.contains("/download") || uri.contains("/linked") || uri.contains("/hello")){
            chain.doFilter(request, response)
            return
        }else if(uri.contains("/actuator/")) {
            chain.doFilter(request, response)
            return
        }else if(uri.equals("/")) {
            chain.doFilter(request, response)
            return
        }

        var token = request.getHeader("Authorization")
        if(token!=null){
            val parts = token.split(" ")
            token = parts[1]
            var claimedUser :String
            if(uri.contains("/judge/")) {
                claimedUser = uri.substringAfter("/judge/")
                claimedUser = claimedUser.subSequence(0, 42) as String
            }else{
                claimedUser = uri.substringAfter("/user/")
                claimedUser = claimedUser.subSequence(0, 42) as String
            }

            //jwt authentication
            try{
                val jws = Jwts.parserBuilder()
                    .setSigningKey(securityConfig.signingKey)
                    .build()
                    .parseClaimsJws(token)

                if(jws.body["sub"]?.equals(claimedUser) == true){
                    chain.doFilter(request, response)
                    return
                }else{
                    response.sendError(403)
                    return
                }
            }catch (ex: JwtException){
                response.sendError(404)
                return
            }
        }
        response.sendError(403)
        return
    }
}