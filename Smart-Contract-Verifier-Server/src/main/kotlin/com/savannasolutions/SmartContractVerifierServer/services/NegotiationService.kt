package com.savannasolutions.SmartContractVerifierServer.services

import com.savannasolutions.SmartContractVerifierServer.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.models.ConditionStatus
import com.savannasolutions.SmartContractVerifierServer.models.Conditions
import com.savannasolutions.SmartContractVerifierServer.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.repositories.ConditionsRepository
import com.savannasolutions.SmartContractVerifierServer.requests.*
import com.savannasolutions.SmartContractVerifierServer.responses.*
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.ArrayList


@Service
class NegotiationService constructor(val agreementsRepository: AgreementsRepository,
                                     val conditionsRepository: ConditionsRepository,
                                     ){

    fun acceptCondition(acceptConditionRequest: AcceptConditionRequest): AcceptConditionResponse{
        if(conditionsRepository.existsById(acceptConditionRequest.conditionID)){
            val condition = conditionsRepository.getById(acceptConditionRequest.conditionID)
            if(condition.conditionStatus == ConditionStatus.PENDING) {
                condition.conditionStatus = ConditionStatus.ACCEPTED
                conditionsRepository.save(condition)
                return AcceptConditionResponse(ResponseStatus.SUCCESSFUL)
            }
            return AcceptConditionResponse(ResponseStatus.FAILED)
        }
        return AcceptConditionResponse(ResponseStatus.FAILED)
    }

    fun createAgreement(createAgreementRequest: CreateAgreementRequest): CreateAgreementResponse{
        if(createAgreementRequest.PartyA.isEmpty())
            return CreateAgreementResponse(null,ResponseStatus.FAILED)

        if(createAgreementRequest.PartyB.isEmpty())
            return CreateAgreementResponse(null,ResponseStatus.FAILED)

        if(createAgreementRequest.PartyB == createAgreementRequest.PartyA)
            return CreateAgreementResponse(null, ResponseStatus.FAILED)

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

        if(agreement.PartyA != createConditionRequest.PreposedUser && agreement.PartyB != createConditionRequest.PreposedUser)
        {
            return CreateConditionResponse(null, ResponseStatus.FAILED)
        }

        var nCondition = Conditions(UUID.randomUUID(),
                                    createConditionRequest.ConditionDescription,
                                    ConditionStatus.PENDING,
                                    createConditionRequest.PreposedUser,
                                    Date(),
                                    agreement)

        nCondition = conditionsRepository.save(nCondition)

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
        val conditions = agreement.conditions;
        val conditionsID = ArrayList<UUID>()
        if (conditions != null) {
            if(conditions.isNotEmpty())
            {
                for(cond in conditions) {
                    conditionsID.add(cond.conditionID)
                }
            }

        }

        return GetAgreementDetailsResponse(agreement.ContractID,
                                            agreement.Duration,
                                            agreement.PartyA,
                                            agreement.PartyB,
                                            agreement.CreatedDate,
                                            agreement.SealedDate,
                                            agreement.MovedToBlockChain,
                                            conditionsID,
                                            ResponseStatus.SUCCESSFUL)
    }

    fun rejectCondition(rejectConditionRequest: RejectConditionRequest): RejectConditionResponse {
        if(conditionsRepository.existsById(rejectConditionRequest.conditionID)){
            val condition = conditionsRepository.getById(rejectConditionRequest.conditionID)
            if(condition.conditionStatus == ConditionStatus.PENDING) {
                condition.conditionStatus = ConditionStatus.REJECTED
                conditionsRepository.save(condition)
                return RejectConditionResponse(ResponseStatus.SUCCESSFUL)
            }
            return RejectConditionResponse(ResponseStatus.FAILED)
        }
        return RejectConditionResponse(ResponseStatus.FAILED)
    }

    fun getAllConditions(getAllConditionsRequest: GetAllConditionsRequest):GetAllConditionsResponse{
        if(!agreementsRepository.existsById(getAllConditionsRequest.AgreementID))
            return GetAllConditionsResponse(null, ResponseStatus.FAILED)

        val conditions = agreementsRepository.getById(getAllConditionsRequest.AgreementID).conditions
        val conditionList = ArrayList<UUID>()
        print("Updated")
        if (conditions != null) {
            for(cond in conditions)
                conditionList.add(cond.conditionID)
        }
        return GetAllConditionsResponse(conditionList, ResponseStatus.SUCCESSFUL)
    }

    fun sealAgreement(sealAgreementRequest: SealAgreementRequest): SealAgreementResponse{
        if(!agreementsRepository.existsById(sealAgreementRequest.AgreementID))
        {
            return SealAgreementResponse(ResponseStatus.FAILED)
        }
        val agreement = agreementsRepository.getById(sealAgreementRequest.AgreementID)
        val condList = agreement.conditions
        if (condList != null) {
            for (cond in condList) {
                if(cond.conditionStatus == ConditionStatus.PENDING)
                    return SealAgreementResponse(ResponseStatus.FAILED)
            }
        }
        agreement.SealedDate = Date()
        agreementsRepository.save(agreement)
        return SealAgreementResponse(ResponseStatus.SUCCESSFUL)
    }

    fun getConditionDetails(getConditionDetailsRequest: GetConditionDetailsRequest): GetConditionDetailsResponse
    {
        if(!conditionsRepository.existsById(getConditionDetailsRequest.conditionID))
            return GetConditionDetailsResponse(getConditionDetailsRequest.conditionID,
                                null,
                                    null,
                                     null,
                                      null,
                                                ResponseStatus.FAILED)

        val condition = conditionsRepository.getById(getConditionDetailsRequest.conditionID)
        return GetConditionDetailsResponse(condition.conditionID,
                                            condition.conditionDescription,
                                            condition.proposingUser,
                                            condition.proposalDate,
                                            condition.contract.ContractID,
                                            ResponseStatus.SUCCESSFUL)
    }

}