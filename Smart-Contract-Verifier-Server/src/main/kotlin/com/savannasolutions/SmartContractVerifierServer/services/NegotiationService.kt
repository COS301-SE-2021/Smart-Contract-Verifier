package com.savannasolutions.SmartContractVerifierServer.services

import com.savannasolutions.SmartContractVerifierServer.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.repositories.ConditionsRepository
import com.savannasolutions.SmartContractVerifierServer.requests.*
import com.savannasolutions.SmartContractVerifierServer.responses.GetAgreementDetailsResponse
import org.springframework.stereotype.Service


@Service
class NegotiationService constructor(val agreementsRepository: AgreementsRepository,
                                     val conditionsRepository: ConditionsRepository,
                                     ){

    fun acceptCondition(acceptConditionRequest: AcceptConditionRequest): AcceptConditionRequest{}

    fun createAgreement(createAgreementRequest: CreateAgreementRequest): CreateAgreementRequest {}

    fun createCondition(createConditionRequest: CreateConditionRequest): CreateConditionRequest{}

    fun getAgreementDetails(getAgreementDetailsRequest: GetAgreementDetailsRequest): GetAgreementDetailsResponse{}

    fun rejectCondition(rejectConditionRequest: RejectConditionRequest): RejectConditionRequest{}
}