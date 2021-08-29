package com.savannasolutions.SmartContractVerifierServer.security.controllers

import com.savannasolutions.SmartContractVerifierServer.security.requests.AddUserRequest
import com.savannasolutions.SmartContractVerifierServer.security.requests.LoginRequest
import com.savannasolutions.SmartContractVerifierServer.security.services.SecurityService
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
class SecurityController(val securityService: SecurityService) {

    @GetMapping("/user/{userId}")
    fun startLogin(@PathVariable userId: String) = securityService.getNonce(userId)

    @PostMapping("/user/{userId}")
    fun login(@PathVariable userId: String, @RequestBody loginRequest: LoginRequest) =
        securityService.login(userId, loginRequest)

    @PostMapping("/user")
    fun addUser(@RequestBody addUserRequest : AddUserRequest) =
        securityService.addUser(addUserRequest)
}