package com.savannasolutions.SmartContractVerifierServer.user.controllers

import com.savannasolutions.SmartContractVerifierServer.user.requests.RetrieveUserAgreementsRequest
import com.savannasolutions.SmartContractVerifierServer.user.services.UserService
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
class UserController constructor(private val userService: UserService) {

    @GetMapping("/user/{userId}/agreement")
    fun retrieveUserAgreements(@PathVariable userId: String,) =
        userService.retrieveUserAgreements(userId)
}