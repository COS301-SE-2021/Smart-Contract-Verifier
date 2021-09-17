package com.savannasolutions.SmartContractVerifierServer.user.controllers

import com.savannasolutions.SmartContractVerifierServer.user.services.UserService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RestController
class UserController constructor(private val userService: UserService) {

    @GetMapping("/user/{userId}/agreement")
    fun retrieveUserAgreements(@PathVariable userId: String,) =
        userService.retrieveUserAgreements(userId)
}