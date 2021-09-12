package com.savannasolutions.SmartContractVerifierServer.UnitTests.evidence

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ApiResponse
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.common.responseErrorMessages.commonResponseErrorMessages
import com.savannasolutions.SmartContractVerifierServer.contracts.repositories.JudgesRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.configuration.EvidenceConfig
import com.savannasolutions.SmartContractVerifierServer.evidence.models.Evidence
import com.savannasolutions.SmartContractVerifierServer.evidence.models.EvidenceType
import com.savannasolutions.SmartContractVerifierServer.evidence.models.LinkedEvidence
import com.savannasolutions.SmartContractVerifierServer.evidence.repositories.EvidenceRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.repositories.LinkedEvidenceRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.repositories.UploadedEvidenceRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.responses.FetchEvidenceResponse
import com.savannasolutions.SmartContractVerifierServer.evidence.services.EvidenceService
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.*
import kotlin.test.assertEquals

internal class FetchEvidenceUnitTest {
    private val agreementsRepository : AgreementsRepository = mock()
    private val userRepository : UserRepository = mock()
    private val evidenceRepository : EvidenceRepository = mock()
    private val linkedEvidenceRepository: LinkedEvidenceRepository = mock()
    private val uploadedEvidenceRepository: UploadedEvidenceRepository = mock()
    private val judgesRepository: JudgesRepository = mock()
    private val evidenceConfig = EvidenceConfig("TEST")


    private val evidenceService = EvidenceService(agreementsRepository = agreementsRepository,
        userRepository = userRepository,
        evidenceRepository = evidenceRepository,
        linkedEvidenceRepository = linkedEvidenceRepository,
        uploadedEvidenceRepository = uploadedEvidenceRepository,
        judgesRepository = judgesRepository,
        evidenceConfig = evidenceConfig)


    private fun parameterizedFetchEvidence(userExists : Boolean,
                                    agreementExists : Boolean,
                                    evidenceExists : Boolean,
                                    userPartOfAgreement: Boolean,
                                    userOwnsEvidence: Boolean): ApiResponse<FetchEvidenceResponse>
    {
        evidenceConfig.initialise()
        evidenceService.initialise()
        //given
        val user = User("test user")
        val otherUser = User("other")
        val agreement = Agreements(
            UUID.fromString("377f66e7-5060-48f8-a44b-ae0bea405a5e"),
            CreatedDate = Date()
        )
        agreement.users.add(user)
        val evidence = Evidence(
            UUID.fromString("7d793c67-10e8-419b-8137-be9758594184"),
            "aUseFulHash",
            EvidenceType.UPLOADED)
        val linkedEvidence = LinkedEvidence(UUID.fromString("523ce05d-aea1-42b4-a405-2ed345e8ecb6"),
                                            "https://dodgy.url")
        if(userOwnsEvidence)
            evidence.user = user
        else
            evidence.user = otherUser

        evidence.contract = agreement
        evidence.evidenceUrl = linkedEvidence
        evidence.removed = false
        linkedEvidence.evidence = evidence

        //when
        whenever(agreementsRepository.existsById(agreement.ContractID)).thenReturn(agreementExists)
        whenever(agreementsRepository.getById(agreement.ContractID)).thenReturn(agreement)
        whenever(userRepository.getById(user.publicWalletID)).thenReturn(user)
        whenever(userRepository.getById(otherUser.publicWalletID)).thenReturn(otherUser)
        whenever(userRepository.existsById(user.publicWalletID)).thenReturn(userExists)
        whenever(userRepository.existsById(otherUser.publicWalletID)).thenReturn(true)
        whenever(evidenceRepository.save(any<Evidence>())).thenReturn(evidence)
        whenever(userRepository.getUsersByAgreementsContaining(agreement)).thenReturn(agreement.users.toList())
        whenever(evidenceRepository.existsById(evidence.evidenceId)).thenReturn(evidenceExists)
        whenever(evidenceRepository.getById(evidence.evidenceId)).thenReturn(evidence)

        //then
        return if(userPartOfAgreement){
            evidenceService.fetchEvidence(user.publicWalletID, agreement.ContractID, evidence.evidenceHash)
        } else
            evidenceService.fetchEvidence(otherUser.publicWalletID, agreement.ContractID, evidence.evidenceHash)
    }

    @Test
    fun `FetchEvidence successful`(){
        //given

        //when
        val response = parameterizedFetchEvidence(userExists = true,
                                                    agreementExists = true,
                                                    evidenceExists = true,
                                                    userPartOfAgreement = true,
                                                    userOwnsEvidence = true)

        //then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
    }

    @Test
    fun `FetchEvidence failed user does not exist`(){
        //given

        //when
        val response = parameterizedFetchEvidence(userExists = false,
            agreementExists = true,
            evidenceExists = true,
            userPartOfAgreement = true,
            userOwnsEvidence = true)

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
        assertEquals(response.message, commonResponseErrorMessages.userDoesNotExist)
    }

    @Test
    fun `FetchEvidence failed agreement does not exist`(){
        //given

        //when
        val response = parameterizedFetchEvidence(userExists = false,
            agreementExists = false,
            evidenceExists = true,
            userPartOfAgreement = true,
            userOwnsEvidence = true)

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
        assertEquals(response.message, commonResponseErrorMessages.agreementDoesNotExist)
    }

    @Test
    fun `FetchEvidence failed evidence does not exist`(){
        //given

        //when
        val response = parameterizedFetchEvidence(userExists = true,
            agreementExists = true,
            evidenceExists = false,
            userPartOfAgreement = true,
            userOwnsEvidence = true)

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
        assertEquals(response.message, commonResponseErrorMessages.evidenceDoesNotExist)
    }

    @Test
    fun `FetchEvidence failed user is not part of the agreement`(){
        //given

        //when
        val response = parameterizedFetchEvidence(userExists = true,
            agreementExists = true,
            evidenceExists = true,
            userPartOfAgreement = false,
            userOwnsEvidence = true)

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
        assertEquals(response.message, commonResponseErrorMessages.userNotPartOfAgreement)
    }

    @Test
    fun `FetchEvidence failed user does not own evidence`(){
        //given

        //when
        val response = parameterizedFetchEvidence(userExists = true,
            agreementExists = true,
            evidenceExists = true,
            userPartOfAgreement = false,
            userOwnsEvidence = true)

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }


}