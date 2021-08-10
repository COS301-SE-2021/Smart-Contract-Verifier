package com.savannasolutions.SmartContractVerifierServer.user.services

import com.savannasolutions.SmartContractVerifierServer.common.AgreementResponse
import com.savannasolutions.SmartContractVerifierServer.common.ConditionResponse
import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.common.UserResponse
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.ConditionsRepository
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import com.savannasolutions.SmartContractVerifierServer.security.requests.AddUserRequest
import com.savannasolutions.SmartContractVerifierServer.user.requests.RetrieveUserAgreementsRequest
import com.savannasolutions.SmartContractVerifierServer.security.requests.UserExistsRequest
import com.savannasolutions.SmartContractVerifierServer.security.responses.AddUserResponse
import com.savannasolutions.SmartContractVerifierServer.user.responses.RetrieveUserAgreementsResponse
import com.savannasolutions.SmartContractVerifierServer.security.responses.UserExistsResponse
import org.springframework.stereotype.Service
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

        if(user.agreements.isEmpty())
            return RetrieveUserAgreementsResponse(emptyList(),ResponseStatus.SUCCESSFUL)

        val agreementList = agreementsRepository.getAllByUsersContaining(user)
        val list = ArrayList<AgreementResponse>()

        if(agreementList != null)
        {
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
                                cond.conditionStatus
                            )
                        )
                    }
                }



                val tempArg = AgreementResponse(agreement.ContractID,
                    agreement.AgreementTitle,
                    agreement.AgreementDescription,
                    agreement.DurationConditionUUID,
                    agreement.PaymentConditionUUID,
                    UserResponse(agreement.users.elementAt(0).publicWalletID),
                    UserResponse(agreement.users.elementAt(1).publicWalletID),
                    agreement.CreatedDate,
                    agreement.SealedDate,
                    agreement.MovedToBlockChain,
                    conditionList,
                    agreement.AgreementImageURL,)

                list.add(tempArg)
            }
        }



        return RetrieveUserAgreementsResponse(list,ResponseStatus.SUCCESSFUL)
    }

}