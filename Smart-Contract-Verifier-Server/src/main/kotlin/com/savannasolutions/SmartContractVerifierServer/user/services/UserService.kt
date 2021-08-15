package com.savannasolutions.SmartContractVerifierServer.user.services

import com.savannasolutions.SmartContractVerifierServer.common.*
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Conditions
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.ConditionsRepository
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import com.savannasolutions.SmartContractVerifierServer.user.requests.RetrieveUserAgreementsRequest
import com.savannasolutions.SmartContractVerifierServer.user.responses.RetrieveUserAgreementsResponse
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.ArrayList

@Service
class UserService(  val userRepository: UserRepository,
                    val agreementsRepository: AgreementsRepository,
                    val conditionsRepository: ConditionsRepository) {


    fun retrieveUserAgreements(retrieveUserAgreementsRequest: RetrieveUserAgreementsRequest): RetrieveUserAgreementsResponse {
        if(retrieveUserAgreementsRequest.UserID.isEmpty())
            return RetrieveUserAgreementsResponse(status = ResponseStatus.FAILED)

        if(!userRepository.existsById(retrieveUserAgreementsRequest.UserID))
            return RetrieveUserAgreementsResponse(status = ResponseStatus.FAILED)

        val user = userRepository.getById(retrieveUserAgreementsRequest.UserID)

        val agreementList = agreementsRepository.getAllByUsersContaining(user)
            ?: return RetrieveUserAgreementsResponse(emptyList(), ResponseStatus.SUCCESSFUL)

        val list = ArrayList<AgreementResponse>()

        for(agreement in agreementList)
        {
            val conditions = conditionsRepository.getAllByContract(agreement)
            val conditionList = ArrayList<ConditionResponse>()
            if(conditions != null)
            {
                for (cond in conditions)
                {
                    conditionList.add(
                        ConditionResponse(cond.conditionID,
                            cond.conditionDescription,
                            UserResponse(cond.proposingUser.publicWalletID),
                            cond.proposalDate,
                            agreement.ContractID,
                            cond.conditionStatus,
                            cond.conditionTitle
                        )
                    )
                }
            }
            val usersList = userRepository.getUsersByAgreementsContaining(agreement)

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


            val tempArg = AgreementResponse(agreement.ContractID,
                agreement.AgreementTitle,
                agreement.AgreementDescription,
                durationConditionResponse,
                paymentConditionResponse,
                UserResponse(usersList.elementAt(0).publicWalletID),
                UserResponse(usersList.elementAt(1).publicWalletID),
                agreement.CreatedDate,
                agreement.SealedDate,
                agreement.MovedToBlockChain,
                conditionList,
                agreement.AgreementImageURL,)

            list.add(tempArg)
        }

        return RetrieveUserAgreementsResponse(list,ResponseStatus.SUCCESSFUL)
    }

}