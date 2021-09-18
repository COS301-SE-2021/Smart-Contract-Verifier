package com.savannasolutions.SmartContractVerifierServer.negotiation.services

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.*
import com.savannasolutions.SmartContractVerifierServer.common.responseErrorMessages.commonResponseErrorMessages
import com.savannasolutions.SmartContractVerifierServer.contracts.repositories.JudgesRepository
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


@Service
class NegotiationService constructor(val agreementsRepository: AgreementsRepository,
                                     val conditionsRepository: ConditionsRepository,
                                     val userRepository: UserRepository,
                                     val judgesRepository: JudgesRepository){

    fun acceptCondition(userID: String, agreementID: UUID, conditionID: UUID): ApiResponse<Objects>{
        if(!conditionsRepository.existsById(conditionID))
            return ApiResponse(status = ResponseStatus.FAILED,
                message = commonResponseErrorMessages.conditionDoesNotExist)

        if(!agreementsRepository.existsById(agreementID))
            return ApiResponse(status = ResponseStatus.FAILED,
                message = commonResponseErrorMessages.agreementDoesNotExist)

        val condition = conditionsRepository.getById(conditionID)
            if(condition.conditionStatus == ConditionStatus.PENDING)
            {
                condition.conditionStatus = ConditionStatus.ACCEPTED
                conditionsRepository.save(condition)
                return ApiResponse(status = ResponseStatus.SUCCESSFUL)
            }
        return ApiResponse(status = ResponseStatus.FAILED,
            message = commonResponseErrorMessages.conditionIsNotPending)
    }

    fun createAgreement(userID: String, createAgreementRequest: CreateAgreementRequest): ApiResponse<CreateAgreementResponse>{
        if(createAgreementRequest.PartyB.isEmpty())
            return ApiResponse(status = ResponseStatus.FAILED,
                message = "Party B is empty")

        if(createAgreementRequest.Title.isEmpty())
            return ApiResponse(status = ResponseStatus.FAILED,
                message = commonResponseErrorMessages.titleIsEmpty)

        if(createAgreementRequest.Description.isEmpty())
            return ApiResponse(status = ResponseStatus.FAILED,
                message = commonResponseErrorMessages.descriptionEmpty)

        if(createAgreementRequest.PartyB == userID)
            return ApiResponse(status = ResponseStatus.FAILED,
                message = "Party A is equal to party A")

        if(!userRepository.existsById(userID))
            return ApiResponse(status = ResponseStatus.FAILED,
                message = commonResponseErrorMessages.userDoesNotExist)

        if(!userRepository.existsById(createAgreementRequest.PartyB))
            return ApiResponse(status = ResponseStatus.FAILED,
            message = "Party B does not exist")

        val userA = userRepository.getById(userID)
        val userB = userRepository.getById(createAgreementRequest.PartyB)

        var nAgreement = Agreements(UUID.randomUUID(),
                                    AgreementTitle = createAgreementRequest.Title,
                                    AgreementDescription = createAgreementRequest.Description,
                                    CreatedDate = Date(),
                                    MovedToBlockChain = false,
                                    AgreementImageURL = createAgreementRequest.ImageURL
                                    )
        nAgreement = nAgreement.apply { users.add(userA)}
        nAgreement = nAgreement.apply { users.add(userB)}

        val tempA = agreementsRepository.getAllByUsersContaining(userA) ?:
        mutableSetOf()

        val tempB = agreementsRepository.getAllByUsersContaining(userB) ?:
        mutableSetOf()

        nAgreement = agreementsRepository.save(nAgreement)

        userA.agreements = tempA
        userB.agreements = tempB

        userA.agreements.add(nAgreement)
        userB.agreements.add(nAgreement)
        userRepository.save(userA)
        userRepository.save(userB)

        return ApiResponse(responseObject = CreateAgreementResponse(nAgreement.ContractID),
            status = ResponseStatus.SUCCESSFUL)
    }

    fun createCondition(userID: String, agreementID: UUID, createConditionRequest: CreateConditionRequest):
            ApiResponse<CreateConditionResponse>{
        if(!agreementsRepository.existsById(agreementID))
            return ApiResponse(status = ResponseStatus.FAILED,
                message = commonResponseErrorMessages.agreementDoesNotExist)

        if(agreementsRepository.getById(agreementID).MovedToBlockChain)
            return ApiResponse(status = ResponseStatus.FAILED,
                message = commonResponseErrorMessages.agreementSealed)

        if(createConditionRequest.ConditionDescription.isEmpty())
            return ApiResponse(status = ResponseStatus.FAILED,
                message = commonResponseErrorMessages.descriptionEmpty)

        if(createConditionRequest.Title.isEmpty())
            return ApiResponse(status = ResponseStatus.FAILED,
                message = commonResponseErrorMessages.titleIsEmpty)

        if(!userRepository.existsById(userID))
            return ApiResponse(status = ResponseStatus.FAILED,
                message = commonResponseErrorMessages.userDoesNotExist)

        val agreement = agreementsRepository.getById(agreementID)

        val users = userRepository.getUsersByAgreementsContaining(agreement)

        if(users.elementAt(0).publicWalletID != userID && users.elementAt(1).publicWalletID != userID)
            return ApiResponse(status = ResponseStatus.FAILED,
                message = commonResponseErrorMessages.userNotPartOfAgreement)

        val user = userRepository.getById(userID)

        var nCondition = Conditions(UUID.randomUUID(),
                                    createConditionRequest.Title,
                                    createConditionRequest.ConditionDescription,
                                    ConditionStatus.PENDING,
                                    Date(),).apply { contract = agreement }
        nCondition = nCondition.apply { proposingUser = user }

        nCondition = conditionsRepository.save(nCondition)

        return ApiResponse(responseObject = CreateConditionResponse(nCondition.conditionID),
            status = ResponseStatus.SUCCESSFUL)
    }

    fun getAgreementDetails(userID: String, AgreementID: UUID): ApiResponse<GetAgreementDetailsResponse>{
        if(!agreementsRepository.existsById(AgreementID))
            return ApiResponse(status = ResponseStatus.FAILED,
                message = commonResponseErrorMessages.agreementDoesNotExist)

        if(!userRepository.existsById(userID))
            return ApiResponse(status = ResponseStatus.FAILED,
                message = commonResponseErrorMessages.userDoesNotExist)

        val agreement = agreementsRepository.getById(AgreementID)

        val userList = userRepository.getUsersByAgreementsContaining(agreement)
        val partyA = UserResponse(userList[0].publicWalletID)
        val partyB = UserResponse(userList[1].publicWalletID)

        if(userID != partyA.PublicWalletID && userID != partyB.PublicWalletID)
            return ApiResponse(status = ResponseStatus.FAILED,
                message = commonResponseErrorMessages.userNotPartOfAgreement)

        val agreementResponse = generateAgreementResponse(agreement)

        return ApiResponse(responseObject = GetAgreementDetailsResponse(agreementResponse),
            status = ResponseStatus.SUCCESSFUL)
    }

    fun rejectCondition(userID: String, agreementID: UUID, conditionID: UUID): ApiResponse<Objects> {
        if(!conditionsRepository.existsById(conditionID))
            return ApiResponse(status = ResponseStatus.FAILED,
            message = commonResponseErrorMessages.conditionDoesNotExist)

        if(!agreementsRepository.existsById(agreementID))
            return ApiResponse(status = ResponseStatus.FAILED,
            message = commonResponseErrorMessages.agreementDoesNotExist)

        val condition = conditionsRepository.getById(conditionID)

        if(condition.contract.ContractID != agreementID)
            return ApiResponse(status = ResponseStatus.FAILED,
            message = "Condition not part of agreement")

        val agreement = agreementsRepository.getById(agreementID)
        val userList = userRepository.getUsersByAgreementsContaining(agreement)

        var found = false
        for(user in userList)
            if(user.publicWalletID == userID)
                found = true

        if(!found)
            return ApiResponse(status = ResponseStatus.FAILED,
            message = commonResponseErrorMessages.userNotPartOfAgreement)

        if(condition.conditionStatus == ConditionStatus.PENDING) {
            condition.conditionStatus = ConditionStatus.REJECTED
            conditionsRepository.save(condition)
            return ApiResponse(status = ResponseStatus.SUCCESSFUL)
        }
        return ApiResponse(status = ResponseStatus.FAILED,
            message = commonResponseErrorMessages.conditionIsNotPending)
    }

    fun getAllConditions(userID:String, agreementID: UUID): ApiResponse<GetAllConditionsResponse>{
        if(!agreementsRepository.existsById(agreementID))
            return ApiResponse(status = ResponseStatus.FAILED,
            message = commonResponseErrorMessages.agreementDoesNotExist)

        val agreement = agreementsRepository.getById(agreementID)
        val userList = userRepository.getUsersByAgreementsContaining(agreement)

        var found = false
        for(user in userList)
            if(user.publicWalletID == userID)
                found = true

        if(!found)
            return ApiResponse(status = ResponseStatus.FAILED,
            message = commonResponseErrorMessages.userNotPartOfAgreement)

        val conditions = conditionsRepository.getAllByContract(agreementsRepository.getById(agreementID))
        conditions?:return ApiResponse(responseObject = GetAllConditionsResponse(emptyList()),
            status = ResponseStatus.SUCCESSFUL)

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
                    cond.conditionTitle
                )
            )
        }
        return ApiResponse(responseObject = GetAllConditionsResponse(conditionList),
            status = ResponseStatus.SUCCESSFUL)
    }

    fun sealAgreement(sealAgreementRequest: SealAgreementRequest): SealAgreementResponse{
        if(!agreementsRepository.existsById(sealAgreementRequest.AgreementID))
        {
            return SealAgreementResponse(ResponseStatus.FAILED)
        }
        val agreement = agreementsRepository.getById(sealAgreementRequest.AgreementID)
        val condList = conditionsRepository.getAllByContract(agreement)
        if (condList != null) {
            for (cond in condList) {
                if(cond.conditionStatus == ConditionStatus.PENDING)
                    return SealAgreementResponse(ResponseStatus.FAILED)
            }
        } else
            return SealAgreementResponse(ResponseStatus.FAILED)

        agreement.DurationConditionUUID?: return SealAgreementResponse(ResponseStatus.FAILED)
        if(conditionsRepository.getById(agreement.DurationConditionUUID!!).conditionStatus == ConditionStatus.PENDING ||
                conditionsRepository.getById(agreement.DurationConditionUUID!!).conditionStatus == ConditionStatus.REJECTED)
                    return SealAgreementResponse(ResponseStatus.FAILED)

        agreement.PaymentConditionUUID?: return SealAgreementResponse(ResponseStatus.FAILED)
        if(conditionsRepository.getById(agreement.PaymentConditionUUID!!).conditionStatus == ConditionStatus.PENDING ||
                conditionsRepository.getById(agreement.PaymentConditionUUID!!).conditionStatus == ConditionStatus.REJECTED)
                    return SealAgreementResponse(ResponseStatus.FAILED)

        agreement.SealedDate = Date()
        agreement.blockchainID = sealAgreementRequest.index
        agreement.MovedToBlockChain = true
        agreementsRepository.save(agreement)
        return SealAgreementResponse(ResponseStatus.SUCCESSFUL)
    }

    fun getConditionDetails(userID: String, agreementID: UUID, conditionID: UUID): ApiResponse<GetConditionDetailsResponse>
    {
        if(!conditionsRepository.existsById(conditionID))
            return ApiResponse(status = ResponseStatus.FAILED,
            message = commonResponseErrorMessages.conditionDoesNotExist)

        val condition = conditionsRepository.getById(conditionID)

        if(!agreementsRepository.existsById(agreementID))
            return ApiResponse(status = ResponseStatus.FAILED,
                message = commonResponseErrorMessages.agreementDoesNotExist)

        if(condition.contract.ContractID != agreementID)
            return ApiResponse(status = ResponseStatus.FAILED,
            message = commonResponseErrorMessages.conditionDoesNotExist)


        val agreement = agreementsRepository.getById(agreementID)

        val userList = userRepository.getUsersByAgreementsContaining(agreement)

        var found = false
        for(user in userList)
            if(user.publicWalletID == userID)
                found = true

        if(!found)
            return ApiResponse(status = ResponseStatus.FAILED,
            message = commonResponseErrorMessages.userNotPartOfAgreement)

        val getConditionDetailsResponse = GetConditionDetailsResponse(
            ConditionResponse(condition.conditionID,
                                            condition.conditionDescription,
                                            UserResponse(condition.proposingUser.publicWalletID),
                                            condition.proposalDate,
                                            condition.contract.ContractID,
                                            condition.conditionStatus,
                                            condition.conditionTitle))

        return ApiResponse(status = ResponseStatus.SUCCESSFUL, responseObject = getConditionDetailsResponse)
    }

    fun setPaymentCondition(userID: String, agreementID: UUID, setPaymentConditionRequest: SetPaymentConditionRequest): ApiResponse<SetPaymentConditionResponse>
    {
        if(setPaymentConditionRequest.PayingUser.isEmpty())
            return ApiResponse(status = ResponseStatus.FAILED,
            message = "Paying user is empty")

        if(!agreementsRepository.existsById(agreementID))
            return ApiResponse(status = ResponseStatus.FAILED,
            message = commonResponseErrorMessages.agreementDoesNotExist)

        if(agreementsRepository.getById(agreementID).MovedToBlockChain)
            return ApiResponse(status = ResponseStatus.FAILED,
                message = commonResponseErrorMessages.agreementSealed)

        if(setPaymentConditionRequest.Payment < 0)
            return ApiResponse(status = ResponseStatus.FAILED,
            message = "Amount is less than 0")

        val agreement = agreementsRepository.getById(agreementID)
        val userList = userRepository.getUsersByAgreementsContaining(agreement)

        if(userID != userList.elementAt(0).publicWalletID
            && userID != userList.elementAt(1).publicWalletID)
            return ApiResponse(status = ResponseStatus.FAILED,
            message = commonResponseErrorMessages.userNotPartOfAgreement)

        if(setPaymentConditionRequest.PayingUser != userList.elementAt(0).publicWalletID
            && setPaymentConditionRequest.PayingUser != userList.elementAt(1).publicWalletID)
            return ApiResponse(status = ResponseStatus.FAILED,
            message = commonResponseErrorMessages.userNotPartOfAgreement)

        val user = userRepository.getById(userID)

        var condition = Conditions(UUID.randomUUID(),
                     "Payment condition",
                "Payment of " + setPaymentConditionRequest.Payment.toString(),
                                ConditionStatus.PENDING,
                                Date(),).apply { contract = agreement }
        condition = condition.apply { proposingUser = user }

        condition = conditionsRepository.save(condition)
        if(agreement.PaymentConditionUUID != null)
        {
            val prevPaymentCondition = conditionsRepository.getById(agreement.PaymentConditionUUID!!)
            prevPaymentCondition.conditionStatus = ConditionStatus.REJECTED
            conditionsRepository.save(prevPaymentCondition)
        }

        agreement.PaymentConditionUUID = condition.conditionID
        agreement.PayingParty = setPaymentConditionRequest.PayingUser

        agreementsRepository.save(agreement)

        return ApiResponse(responseObject = SetPaymentConditionResponse(condition.conditionID),
            status = ResponseStatus.SUCCESSFUL)
    }

    fun setDurationCondition(userID: String, agreementID: UUID,setDurationConditionRequest: SetDurationConditionRequest):
            ApiResponse<SetDurationConditionResponse>
    {
        if(!agreementsRepository.existsById(agreementID))
            return ApiResponse(status = ResponseStatus.FAILED,
            message = commonResponseErrorMessages.agreementDoesNotExist)

        if(agreementsRepository.getById(agreementID).MovedToBlockChain)
            return ApiResponse(status = ResponseStatus.FAILED,
                message = commonResponseErrorMessages.agreementSealed)

        if(setDurationConditionRequest.Duration.isNegative || setDurationConditionRequest.Duration.isZero)
            return ApiResponse(status = ResponseStatus.FAILED,
            message = "Duration is negative or zero")

        if(!userRepository.existsById(userID))
            return ApiResponse(status = ResponseStatus.FAILED,
            message = commonResponseErrorMessages.userDoesNotExist)

        val agreement = agreementsRepository.getById(agreementID)
        val userList = userRepository.getUsersByAgreementsContaining(agreement)

        if(userList.elementAt(0).publicWalletID != userID &&
            userList.elementAt(1).publicWalletID != userID)
            return ApiResponse(status = ResponseStatus.FAILED,
            message = commonResponseErrorMessages.userNotPartOfAgreement)

        val user = userRepository.getById(userID)

        var condition = Conditions(UUID.randomUUID(),
                "Duration condition",
                "Duration of " + setDurationConditionRequest.Duration.toSeconds().toString(),
                ConditionStatus.PENDING,
                Date(),).apply { contract = agreement }

        condition = condition.apply { proposingUser = user}

        condition = conditionsRepository.save(condition)

        if(agreement.DurationConditionUUID != null)
        {
            val prevDurationCondition = conditionsRepository.getById(agreement.DurationConditionUUID!!)
            prevDurationCondition.conditionStatus = ConditionStatus.REJECTED
            conditionsRepository.save(prevDurationCondition)
        }

        agreement.DurationConditionUUID = condition.conditionID

        agreementsRepository.save(agreement)

        return ApiResponse(responseObject = SetDurationConditionResponse(condition.conditionID),
            status = ResponseStatus.SUCCESSFUL)
    }

    fun getJudgingAgreements(userID: String): ApiResponse<GetJudgingAgreementsResponse>
    {
        if(!userRepository.existsById(userID))
            return ApiResponse(status = ResponseStatus.FAILED,
            message = commonResponseErrorMessages.userDoesNotExist)

        val user = userRepository.getById(userID)
        val judgeList = judgesRepository.getAllByJudge(user) ?:
            return ApiResponse(responseObject = GetJudgingAgreementsResponse(emptyList()),
                status = ResponseStatus.SUCCESSFUL)

        val agreementsResponseList = ArrayList<AgreementResponse>()

        for(judge in judgeList)
        {
            agreementsResponseList.add(generateAgreementResponse(judge.agreement))
        }

        return ApiResponse(responseObject = GetJudgingAgreementsResponse(agreementsResponseList),
            status = ResponseStatus.SUCCESSFUL)
    }

    private fun generateAgreementResponse(agreement: Agreements): AgreementResponse {
        val conditionList = conditionsRepository.getAllByContract(agreement)
        val conditions = ArrayList<ConditionResponse>()
        if (conditionList != null) {
            for (cond in conditionList) {
                val tempCond = ConditionResponse(
                    cond.conditionID,
                    cond.conditionDescription,
                    UserResponse(cond.proposingUser.publicWalletID),
                    cond.proposalDate,
                    agreement.ContractID,
                    cond.conditionStatus,
                    cond.conditionTitle
                )
                conditions.add(tempCond)
            }
        }

        val userList = userRepository.getUsersByAgreementsContaining(agreement)
        val partyA = UserResponse(userList[0].publicWalletID)
        val partyB = UserResponse(userList[1].publicWalletID)
        val paymentCondition : Conditions? = if(agreement.PaymentConditionUUID != null)
            conditionsRepository.getById(agreement.PaymentConditionUUID!!)
        else null

        val durationCondition : Conditions? = if(agreement.DurationConditionUUID != null)
            conditionsRepository.getById(agreement.DurationConditionUUID!!)
        else null

        val paymentConditionResponse : PaymentConditionResponse?
        if(paymentCondition != null)
        {
            var amountStr = paymentCondition.conditionDescription
            amountStr = amountStr.replace("Payment of ", "")
            val amount = amountStr.toDouble()
            paymentConditionResponse = PaymentConditionResponse(paymentCondition.conditionID,
                amount,
                agreement.PayingParty!!,
                paymentCondition.conditionStatus)
        } else
            paymentConditionResponse = null

        val durationConditionResponse : DurationConditionResponse?
        if(durationCondition != null)
        {
            var amountStr = durationCondition.conditionDescription
            amountStr = amountStr.replace("Duration of ", "")
            val amount  = amountStr.toDouble()
            durationConditionResponse = DurationConditionResponse(durationCondition.conditionID,
                amount,
                durationCondition.conditionStatus)
        } else
            durationConditionResponse = null

        return AgreementResponse(
            agreement.ContractID,
            agreement.AgreementTitle,
            agreement.AgreementDescription,
            durationConditionResponse,
            paymentConditionResponse,
            partyA,
            partyB,
            agreement.CreatedDate,
            agreement.SealedDate,
            agreement.MovedToBlockChain,
            conditions,
            agreement.AgreementImageURL,
            agreement.blockchainID
        )
    }

}