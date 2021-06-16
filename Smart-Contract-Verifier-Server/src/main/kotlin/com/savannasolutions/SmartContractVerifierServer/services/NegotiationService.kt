package com.savannasolutions.SmartContractVerifierServer.services

import com.savannasolutions.SmartContractVerifierServer.models.ConditionStatus
import com.savannasolutions.SmartContractVerifierServer.models.Conditions
import com.savannasolutions.SmartContractVerifierServer.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.repositories.ConditionsRepository
import com.savannasolutions.SmartContractVerifierServer.requests.*
import com.savannasolutions.SmartContractVerifierServer.responses.*
import org.springframework.stereotype.Service


@Service
class NegotiationService constructor(val agreementsRepository: AgreementsRepository,
                                     val conditionsRepository: ConditionsRepository,
                                     ){

    fun acceptCondition(acceptConditionRequest: AcceptConditionRequest): AcceptConditionResponse{
        if(conditionsRepository.existsById(acceptConditionRequest.conditionID)){
            conditionsRepository.getById(acceptConditionRequest.conditionID).conditionStatus = ConditionStatus.ACCEPTED
            return AcceptConditionResponse(ResponseStatus.SUCCESSFUL)
        }
        return AcceptConditionResponse(ResponseStatus.FAILED)
    }

    fun createAgreement(createAgreementRequest: CreateAgreementRequest): CreateAgreementRequest? = null

    fun createCondition(createConditionRequest: CreateConditionRequest): CreateConditionRequest? = null

    fun getAgreementDetails(getAgreementDetailsRequest: GetAgreementDetailsRequest): GetAgreementDetailsResponse? = null

    fun rejectCondition(rejectConditionRequest: RejectConditionRequest): RejectConditionResponse {
        if(conditionsRepository.existsById(rejectConditionRequest.conditionID)){
            conditionsRepository.getById(rejectConditionRequest.conditionID).conditionStatus = ConditionStatus.REJECTED
            return RejectConditionResponse(ResponseStatus.SUCCESSFUL)
        }
        return RejectConditionResponse(ResponseStatus.FAILED)
    }

    fun getAllConditions(getAllConditionsRequest: GetAllConditionsRequest):GetAllConditionsResponse{
        if(agreementsRepository.existsById(getAllConditionsRequest.AgreementID))
            return GetAllConditionsResponse(agreementsRepository.getById(getAllConditionsRequest.AgreementID).conditions)
        return GetAllConditionsResponse(emptyList())
    }

    fun sealAgreement(sealAgreementRequest: SealAgreementRequest): SealAgreementResponse? = null
}