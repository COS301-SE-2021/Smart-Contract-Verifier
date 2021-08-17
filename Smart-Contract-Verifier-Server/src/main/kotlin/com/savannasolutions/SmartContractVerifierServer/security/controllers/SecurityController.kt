package com.savannasolutions.SmartContractVerifierServer.security.controllers

import com.savannasolutions.SmartContractVerifierServer.security.requests.AddUserRequest
import com.savannasolutions.SmartContractVerifierServer.security.requests.LoginRequest
import com.savannasolutions.SmartContractVerifierServer.security.requests.UserExistsRequest
import com.savannasolutions.SmartContractVerifierServer.security.services.SecurityService
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
class SecurityController(val securityService: SecurityService) {

    @GetMapping("/login/{id}")
    fun startLogin(@PathVariable id: String) = securityService.getNonce(id)

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest) =
        securityService.login(loginRequest)

    @PostMapping("/add-user")
    fun addUser(@RequestBody addUserRequest : AddUserRequest) =
        securityService.addUser(addUserRequest)

    @PostMapping("/user-exists")
    fun userExists(@RequestBody userExistsRequest: UserExistsRequest) =
        securityService.userExists(userExistsRequest)
}