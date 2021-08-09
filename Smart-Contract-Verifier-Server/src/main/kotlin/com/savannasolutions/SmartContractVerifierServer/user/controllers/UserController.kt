package com.savannasolutions.SmartContractVerifierServer.user.controllers

import com.savannasolutions.SmartContractVerifierServer.user.requests.AddUserRequest
import com.savannasolutions.SmartContractVerifierServer.user.requests.GetNonceRequest
import com.savannasolutions.SmartContractVerifierServer.user.requests.RetrieveUserAgreementsRequest
import com.savannasolutions.SmartContractVerifierServer.user.requests.UserExistsRequest
import com.savannasolutions.SmartContractVerifierServer.user.responses.AddUserResponse
import com.savannasolutions.SmartContractVerifierServer.user.responses.RetrieveUserAgreementsResponse
import com.savannasolutions.SmartContractVerifierServer.user.services.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController constructor(private val userService: UserService) {

    @PostMapping("/add-user")
    fun addUser(@RequestBody addUserRequest : AddUserRequest) =
        userService.addUser(addUserRequest)

    @PostMapping("/retrieve-user-agreements")
    fun retrieveUserAgreements(@RequestBody retrieveUserAgreementsRequest: RetrieveUserAgreementsRequest) =
        userService.retrieveUserAgreements(retrieveUserAgreementsRequest)

    @PostMapping("/user-exists")
    fun userExists(@RequestBody userExistsRequest: UserExistsRequest) =
        userService.userExists(userExistsRequest)

    @PostMapping("/nonce")
    fun getUserNonce(@RequestBody getNonceRequest: GetNonceRequest) =
        userService.getNonce(getNonceRequest)
}