package com.savannasolutions.SmartContractVerifierServer.common.responseErrorMessages

class commonResponseErrorMessages {
    companion object{
        const val userDoesNotExist = "User does not exist in database"
        const val agreementDoesNotExist = "Agreement does not exist in database"
        const val messageDoesNotExist = "Message does not exist in database"
        const val userNotPartOfAgreement = "User not part of the provided agreement"
        const val conditionDoesNotExist = "Condition does not exist in database"
        const val conditionIsNotPending = "Condition is not pending"
        const val titleIsEmpty = "Title is empty"
        const val descriptionEmpty = "Description is empty"
        const val contactListDoesNotExist = "Contact List does not exist in database"
        const val evidenceDoesNotExist = "Evidence does not exist"
        const val agreementSealed = "Agreement negotiation has concluded"
    }
}