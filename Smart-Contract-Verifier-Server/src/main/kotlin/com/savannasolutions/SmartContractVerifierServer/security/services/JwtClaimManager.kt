package com.savannasolutions.SmartContractVerifierServer.security.services

class JwtClaimManager {
    fun validateUserId(userId: String, jwtToken: String): Boolean {
        return false
    }

    fun validateUserByAgreement(agreementId: String, jwtToken: String): Boolean{
        return false
    }
}