package com.savannasolutions.SmartContractVerifierServer.common.responseErrorMessages

class commonResponseErrorMessages {
    companion object{
        const val userDoesNotExist = "User does not exist in database"
        const val agreementDoesNotExist = "Agreement does not exist in database"
        const val messageDoesNotExist = "Message does not exist in database"
        const val userNotPartOfAgreement = "User not part of the provided agreement"
    }
}