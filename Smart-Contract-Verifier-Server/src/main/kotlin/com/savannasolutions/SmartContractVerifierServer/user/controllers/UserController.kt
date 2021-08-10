package com.savannasolutions.SmartContractVerifierServer.user.controllers

import com.savannasolutions.SmartContractVerifierServer.security.requests.AddUserRequest
import com.savannasolutions.SmartContractVerifierServer.user.requests.RetrieveUserAgreementsRequest
import com.savannasolutions.SmartContractVerifierServer.security.requests.UserExistsRequest
import com.savannasolutions.SmartContractVerifierServer.user.services.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController constructor(private val userService: UserService) {



    @PostMapping("/retrieve-user-agreements")
    fun retrieveUserAgreements(@RequestBody retrieveUserAgreementsRequest: RetrieveUserAgreementsRequest) =
        userService.retrieveUserAgreements(retrieveUserAgreementsRequest)


}