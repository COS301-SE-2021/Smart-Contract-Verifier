package com.savannasolutions.SmartContractVerifierServer.services

import com.savannasolutions.SmartContractVerifierServer.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.repositories.ConditionsRepository
import com.savannasolutions.SmartContractVerifierServer.requests.*
import com.savannasolutions.SmartContractVerifierServer.responses.GetAgreementDetailsResponse
import com.savannasolutions.SmartContractVerifierServer.responses.GetAllConditionsResponse
import com.savannasolutions.SmartContractVerifierServer.responses.SealAgreementResponse
import org.springframework.stereotype.Service


@Service
class NegotiationService constructor(val agreementsRepository: AgreementsRepository,
                                     val conditionsRepository: ConditionsRepository,
                                     ){

    fun acceptCondition(acceptConditionRequest: AcceptConditionRequest): AcceptConditionRequest? = null

    fun createAgreement(createAgreementRequest: CreateAgreementRequest): CreateAgreementRequest? = null

    fun createCondition(createConditionRequest: CreateConditionRequest): CreateConditionRequest? = null

    fun getAgreementDetails(getAgreementDetailsRequest: GetAgreementDetailsRequest): GetAgreementDetailsResponse? = null

    fun rejectCondition(rejectConditionRequest: RejectConditionRequest): RejectConditionRequest? = null

    fun getAllConditions(getAllConditionsRequest: GetAllConditionsRequest):GetAllConditionsResponse? = null

    fun sealAgreement(sealAgreementRequest: SealAgreementRequest): SealAgreementResponse? = null
}