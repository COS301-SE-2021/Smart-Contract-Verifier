package com.savannasolutions.SmartContractVerifierServer.services

import com.savannasolutions.SmartContractVerifierServer.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.models.ConditionStatus
import com.savannasolutions.SmartContractVerifierServer.models.Conditions
import com.savannasolutions.SmartContractVerifierServer.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.repositories.ConditionsRepository
import com.savannasolutions.SmartContractVerifierServer.repositories.UserRepository
import com.savannasolutions.SmartContractVerifierServer.requests.*
import com.savannasolutions.SmartContractVerifierServer.responses.*
import org.springframework.stereotype.Service
import java.util.*


@Service
class NegotiationService constructor(val agreementsRepository: AgreementsRepository,
                                     val conditionsRepository: ConditionsRepository,
                                     val userRepository: UserRepository,){

    fun acceptCondition(acceptConditionRequest: AcceptConditionRequest): AcceptConditionResponse{
        if(conditionsRepository.existsById(acceptConditionRequest.conditionID)){
            var condition = conditionsRepository.getById(acceptConditionRequest.conditionID)
            condition.conditionStatus = ConditionStatus.ACCEPTED
            conditionsRepository.save(condition)
            return AcceptConditionResponse(ResponseStatus.SUCCESSFUL)
        }
        return AcceptConditionResponse(ResponseStatus.FAILED)
    }

    fun createAgreement(createAgreementRequest: CreateAgreementRequest): CreateAgreementResponse{
        if(createAgreementRequest.PartyA.isEmpty())
            return CreateAgreementResponse(null,ResponseStatus.FAILED)

        if(createAgreementRequest.PartyB.isEmpty())
            return CreateAgreementResponse(null,ResponseStatus.FAILED)

        var nAgreement = Agreements(UUID.randomUUID(),
                                    null,
                                    createAgreementRequest.PartyA,
                                    createAgreementRequest.PartyB,
                                    Date(),
                                    null,
                                    null,
                                    false,
                                    null,
                                    null,)

        nAgreement = agreementsRepository.save(nAgreement)

        return CreateAgreementResponse(nAgreement.ContractID,ResponseStatus.SUCCESSFUL)
    }

    fun createCondition(createConditionRequest: CreateConditionRequest): CreateConditionResponse{
        if(!agreementsRepository.existsById(createConditionRequest.AgreementID))
        {
            return CreateConditionResponse(null, ResponseStatus.FAILED)
        }
        if(createConditionRequest.ConditionDescription.isEmpty())
        {
            return CreateConditionResponse(null, ResponseStatus.FAILED)
        }
        if(createConditionRequest.PreposedUser.isEmpty())
        {
            return CreateConditionResponse(null, ResponseStatus.FAILED)
        }
        val agreement = agreementsRepository.getById(createConditionRequest.AgreementID)

        val nCondition = Conditions(UUID.randomUUID(),
                                    createConditionRequest.ConditionDescription,
                                    ConditionStatus.PENDING,
                                    createConditionRequest.PreposedUser,
                                    Date(),
                                    agreement)

        conditionsRepository.save(nCondition)

        return CreateConditionResponse(nCondition.conditionID, ResponseStatus.SUCCESSFUL)
    }

    fun getAgreementDetails(getAgreementDetailsRequest: GetAgreementDetailsRequest): GetAgreementDetailsResponse{
        if(!agreementsRepository.existsById(getAgreementDetailsRequest.AgreementID))
        {
            return GetAgreementDetailsResponse(getAgreementDetailsRequest.AgreementID,
                                            null,
                                            null,
                                            null,
                                            null,
                                            null,
                                            null,
                                            null,
                                            ResponseStatus.FAILED)
        }
        val agreement = agreementsRepository.getById(getAgreementDetailsRequest.AgreementID)
        return GetAgreementDetailsResponse(agreement.ContractID,
                                            agreement.Duration,
                                            agreement.PartyA,
                                            agreement.PartyB,
                                            agreement.CreatedDate,
                                            agreement.SealedDate,
                                            agreement.MovedToBlockChain,
                                            agreement.conditions,
                                            ResponseStatus.SUCCESSFUL)
    }

    fun rejectCondition(rejectConditionRequest: RejectConditionRequest): RejectConditionResponse {
        if(conditionsRepository.existsById(rejectConditionRequest.conditionID)){
            var condition = conditionsRepository.getById(rejectConditionRequest.conditionID)
            condition.conditionStatus = ConditionStatus.REJECTED
            conditionsRepository.save(condition)
            return RejectConditionResponse(ResponseStatus.SUCCESSFUL)
        }
        return RejectConditionResponse(ResponseStatus.FAILED)
    }

    fun getAllConditions(getAllConditionsRequest: GetAllConditionsRequest):GetAllConditionsResponse{
        if(agreementsRepository.existsById(getAllConditionsRequest.AgreementID))
            return GetAllConditionsResponse(agreementsRepository.getById(getAllConditionsRequest.AgreementID).conditions, ResponseStatus.SUCCESSFUL)
        return GetAllConditionsResponse(emptyList(), ResponseStatus.FAILED)
    }

    fun sealAgreement(sealAgreementRequest: SealAgreementRequest): SealAgreementResponse{
        if(!agreementsRepository.existsById(sealAgreementRequest.AgreementID))
        {
            return SealAgreementResponse(ResponseStatus.FAILED)
        }
        val agreement = agreementsRepository.getById(sealAgreementRequest.AgreementID)
        agreement.SealedDate = Date()
        agreementsRepository.save(agreement)
        return SealAgreementResponse(ResponseStatus.SUCCESSFUL)
    }
}