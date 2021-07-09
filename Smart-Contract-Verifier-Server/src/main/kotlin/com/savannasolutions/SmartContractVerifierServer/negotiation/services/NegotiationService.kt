package com.savannasolutions.SmartContractVerifierServer.negotiation.services

import com.savannasolutions.SmartContractVerifierServer.common.AgreementResponse
import com.savannasolutions.SmartContractVerifierServer.common.ConditionResponse
import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.common.UserResponse
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.ConditionStatus
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Conditions
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.ConditionsRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.requests.*
import com.savannasolutions.SmartContractVerifierServer.negotiation.responses.*
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.ArrayList


@Service
class NegotiationService constructor(val agreementsRepository: AgreementsRepository,
                                     val conditionsRepository: ConditionsRepository,
                                     val userRepository: UserRepository,
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
            return CreateAgreementResponse(status = ResponseStatus.FAILED)

        if(createAgreementRequest.PartyB.isEmpty())
            return CreateAgreementResponse(status = ResponseStatus.FAILED)

        if(createAgreementRequest.Title.isEmpty())
            return CreateAgreementResponse(status = ResponseStatus.FAILED)

        if(createAgreementRequest.Description.isEmpty())
            return CreateAgreementResponse(status = ResponseStatus.FAILED)

        if(createAgreementRequest.PartyB == createAgreementRequest.PartyA)
            return CreateAgreementResponse(status = ResponseStatus.FAILED)

        if(!userRepository.existsById(createAgreementRequest.PartyA))
            return CreateAgreementResponse(status = ResponseStatus.FAILED)

        if(!userRepository.existsById(createAgreementRequest.PartyB))
            return CreateAgreementResponse(status = ResponseStatus.FAILED)

        val userA = userRepository.getById(createAgreementRequest.PartyA)
        val userB = userRepository.getById(createAgreementRequest.PartyB)

        var nAgreement = Agreements(UUID.randomUUID(),
                                    AgreementTitle = createAgreementRequest.Title,
                                    AgreementDescription = createAgreementRequest.Description,
                                    CreatedDate = Date(),
                                    MovedToBlockChain = false,
                                    AgreementImageURL = createAgreementRequest.ImageURL
                                    ).apply { partyA = userA}
        nAgreement = nAgreement.apply { partyB = userB }

        nAgreement = agreementsRepository.save(nAgreement)

        return CreateAgreementResponse(nAgreement.ContractID, ResponseStatus.SUCCESSFUL)
    }

    fun createCondition(createConditionRequest: CreateConditionRequest): CreateConditionResponse{
        if(!agreementsRepository.existsById(createConditionRequest.AgreementID))
            return CreateConditionResponse(status = ResponseStatus.FAILED)

        if(createConditionRequest.ConditionDescription.isEmpty())
            return CreateConditionResponse(status = ResponseStatus.FAILED)

        if(createConditionRequest.PreposedUser.isEmpty())
            return CreateConditionResponse(status = ResponseStatus.FAILED)

        if(createConditionRequest.Title.isEmpty())
            return CreateConditionResponse(status = ResponseStatus.FAILED)


        if(!userRepository.existsById(createConditionRequest.PreposedUser))
            return CreateConditionResponse(status = ResponseStatus.FAILED)

        val agreement = agreementsRepository.getById(createConditionRequest.AgreementID)

        if(agreement.partyA.publicWalletID != createConditionRequest.PreposedUser && agreement.partyB.publicWalletID != createConditionRequest.PreposedUser)
            return CreateConditionResponse(status = ResponseStatus.FAILED)

        val user = userRepository.getById(createConditionRequest.PreposedUser)

        var nCondition = Conditions(UUID.randomUUID(),
                                    createConditionRequest.Title,
                                    createConditionRequest.ConditionDescription,
                                    ConditionStatus.PENDING,
                                    Date(),).apply { contract = agreement }
        nCondition = nCondition.apply { proposingUser = user }

        nCondition = conditionsRepository.save(nCondition)

        return CreateConditionResponse(nCondition.conditionID, ResponseStatus.SUCCESSFUL)
    }

    fun getAgreementDetails(getAgreementDetailsRequest: GetAgreementDetailsRequest): GetAgreementDetailsResponse{
        if(!agreementsRepository.existsById(getAgreementDetailsRequest.AgreementID))
        {
            return GetAgreementDetailsResponse(status = ResponseStatus.FAILED)
        }
        val agreement = agreementsRepository.getById(getAgreementDetailsRequest.AgreementID)
        val conditionList = conditionsRepository.getAllByContract(agreement)
        val conditions = ArrayList<ConditionResponse>()
        for(cond in conditionList)
        {
            val tempCond = ConditionResponse(cond.conditionID,
                cond.conditionDescription,
                UserResponse(cond.proposingUser.publicWalletID),
                cond.proposalDate,
                agreement.ContractID,
                cond.conditionStatus,)
            conditions.add(tempCond)
        }

        val partyA = UserResponse(agreement.partyA.publicWalletID)
        val partyB = UserResponse(agreement.partyB.publicWalletID)

        val agreementResponse = AgreementResponse(agreement.ContractID,
                                                    agreement.AgreementTitle,
                                                    agreement.AgreementDescription,
                                                    agreement.DurationConditionUUID,
                                                    agreement.PaymentConditionUUID,
                                                    partyA,
                                                    partyB,
                                                    agreement.CreatedDate,
                                                    agreement.SealedDate,
                                                    agreement.MovedToBlockChain,
                                                    conditions,
                                                    agreement.AgreementImageURL)

        return GetAgreementDetailsResponse(agreementResponse,ResponseStatus.SUCCESSFUL)
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

        val conditions = conditionsRepository.getAllByContract(agreementsRepository.getById(getAllConditionsRequest.AgreementID))
        val conditionList = ArrayList<ConditionResponse>()
        for(cond in conditions)
        {
            conditionList.add(
                ConditionResponse(cond.conditionID,
                    cond.conditionDescription,
                    UserResponse(cond.proposingUser.publicWalletID),
                    cond.proposalDate,
                    cond.contract.ContractID,
                    cond.conditionStatus,
                    cond.conditionTitle)
            )
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

        agreement.DurationConditionUUID?: return SealAgreementResponse(ResponseStatus.FAILED)
        if(conditionsRepository.getById(agreement.DurationConditionUUID!!).conditionStatus == ConditionStatus.PENDING ||
                conditionsRepository.getById(agreement.DurationConditionUUID!!).conditionStatus == ConditionStatus.REJECTED)
                    return SealAgreementResponse(ResponseStatus.FAILED)

        agreement.PaymentConditionUUID?: return SealAgreementResponse(ResponseStatus.FAILED)
        if(conditionsRepository.getById(agreement.PaymentConditionUUID!!).conditionStatus == ConditionStatus.PENDING ||
                conditionsRepository.getById(agreement.PaymentConditionUUID!!).conditionStatus == ConditionStatus.REJECTED)
                    return SealAgreementResponse(ResponseStatus.FAILED)

        agreement.SealedDate = Date()
        agreementsRepository.save(agreement)
        return SealAgreementResponse(ResponseStatus.SUCCESSFUL)
    }

    fun getConditionDetails(getConditionDetailsRequest: GetConditionDetailsRequest): GetConditionDetailsResponse
    {
        if(!conditionsRepository.existsById(getConditionDetailsRequest.conditionID))
            return GetConditionDetailsResponse(getConditionDetailsRequest.conditionID,
                                                status = ResponseStatus.FAILED)

        val condition = conditionsRepository.getById(getConditionDetailsRequest.conditionID)
        return GetConditionDetailsResponse(condition.conditionID,
                                            condition.conditionDescription,
                                            condition.proposingUser.publicWalletID,
                                            condition.proposalDate,
                                            condition.contract.ContractID,
                                            condition.conditionStatus,

                                            ResponseStatus.SUCCESSFUL)
    }

    fun setPaymentCondition(setPaymentConditionRequest: SetPaymentConditionRequest): SetPaymentConditionResponse
    {
        if(!agreementsRepository.existsById(setPaymentConditionRequest.AgreementID))
            return SetPaymentConditionResponse(null, ResponseStatus.FAILED)

        if(setPaymentConditionRequest.Payment < 0)
            return SetPaymentConditionResponse(null, ResponseStatus.FAILED)

        val agreement = agreementsRepository.getById(setPaymentConditionRequest.AgreementID)

        if(setPaymentConditionRequest.PreposedUser != agreement.partyA.publicWalletID && setPaymentConditionRequest.PreposedUser != agreement.partyB.publicWalletID)
            return SetPaymentConditionResponse(null, ResponseStatus.FAILED)

        val user = userRepository.getById(setPaymentConditionRequest.PreposedUser)

        var condition = Conditions(UUID.randomUUID(),
                     "Payment condition",
                "Payment of " + setPaymentConditionRequest.Payment.toString(),
                                ConditionStatus.PENDING,
                                Date(),).apply { contract = agreement }
        condition = condition.apply { proposingUser = user }

        condition = conditionsRepository.save(condition)

        agreement.PaymentConditionUUID = condition.conditionID

        agreementsRepository.save(agreement)

        return SetPaymentConditionResponse(condition.conditionID, ResponseStatus.SUCCESSFUL)
    }

    fun setDurationCondition(setDurationConditionRequest: SetDurationConditionRequest): SetDurationConditionResponse
    {
        if(!agreementsRepository.existsById(setDurationConditionRequest.AgreementID))
            return SetDurationConditionResponse(status = ResponseStatus.FAILED)

        if(setDurationConditionRequest.Duration.isNegative || setDurationConditionRequest.Duration.isZero)
            return SetDurationConditionResponse(status = ResponseStatus.FAILED)

        if(!userRepository.existsById(setDurationConditionRequest.PreposedUser))
            return SetDurationConditionResponse(status = ResponseStatus.FAILED)

        val agreement = agreementsRepository.getById(setDurationConditionRequest.AgreementID)

        if(agreement.partyA.publicWalletID != setDurationConditionRequest.PreposedUser && agreement.partyB.publicWalletID != setDurationConditionRequest.PreposedUser)
            return SetDurationConditionResponse(status = ResponseStatus.FAILED)

        val user = userRepository.getById(setDurationConditionRequest.PreposedUser)

        var condition = Conditions(UUID.randomUUID(),
                "Duration condition",
                "Duration of " + setDurationConditionRequest.Duration.toString(),
                ConditionStatus.PENDING,
                Date(),).apply { contract = agreement }

        condition = condition.apply { proposingUser = user}

        condition = conditionsRepository.save(condition)

        agreement.DurationConditionUUID = condition.conditionID

        agreementsRepository.save(agreement)

        return SetDurationConditionResponse(condition.conditionID, ResponseStatus.SUCCESSFUL)
    }

}