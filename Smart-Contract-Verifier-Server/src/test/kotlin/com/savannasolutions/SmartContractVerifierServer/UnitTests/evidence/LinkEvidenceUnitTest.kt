package com.savannasolutions.SmartContractVerifierServer.UnitTests.evidence

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ApiResponse
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.contracts.repositories.JudgesRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.configuration.EvidenceConfig
import com.savannasolutions.SmartContractVerifierServer.evidence.models.Evidence
import com.savannasolutions.SmartContractVerifierServer.evidence.models.EvidenceType
import com.savannasolutions.SmartContractVerifierServer.evidence.models.UploadedEvidence
import com.savannasolutions.SmartContractVerifierServer.evidence.repositories.EvidenceRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.repositories.LinkedEvidenceRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.repositories.UploadedEvidenceRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.requests.LinkEvidenceRequest
import com.savannasolutions.SmartContractVerifierServer.evidence.services.EvidenceService
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.util.*
import kotlin.io.path.Path
import kotlin.test.assertEquals

internal class LinkEvidenceUnitTest {
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

    private fun parameterizeLinkEvidence(userExists: Boolean,
                                        agreementExists : Boolean,
                                        userPartOfAgreement: Boolean,
                                        evidenceURL: String): ApiResponse<Objects>
    {
        evidenceConfig.initialise()
        evidenceService.initialise()
        //given
        val user = User("test user")
        val otherUser = User("other")
        val agreement = Agreements(UUID.fromString("377f66e7-5060-48f8-a44b-ae0bea405a5e"),
            CreatedDate = Date())
        agreement.users.add(user)
        val uploadedEvidence = UploadedEvidence(UUID.fromString("1981c189-afb4-431a-9fc5-d8e2e48b7110"),
            "testFile",
            "txt",
            "testFile")
        val evidence = Evidence("aUseFulHash",
            EvidenceType.UPLOADED)
        evidence.user = user
        evidence.contract = agreement
        evidence.uploadedEvidence = uploadedEvidence
        uploadedEvidence.evidence = evidence

        //when
        whenever(agreementsRepository.existsById(agreement.ContractID)).thenReturn(agreementExists)
        whenever(agreementsRepository.getById(agreement.ContractID)).thenReturn(agreement)
        whenever(userRepository.getById(user.publicWalletID)).thenReturn(user)
        whenever(userRepository.getById(otherUser.publicWalletID)).thenReturn(otherUser)
        whenever(userRepository.existsById(user.publicWalletID)).thenReturn(userExists)
        whenever(userRepository.existsById(otherUser.publicWalletID)).thenReturn(true)


        //then
        val request = LinkEvidenceRequest(evidenceURL)
        return if(userPartOfAgreement){
            evidenceService.linkEvidence(user.publicWalletID, agreement.ContractID, request)
        } else
            evidenceService.linkEvidence(otherUser.publicWalletID, agreement.ContractID, request)
    }

    @Test
    fun `LinkEvidence successful`(){
        //given

        //when
        val response = parameterizeLinkEvidence(userExists = true,
                                                agreementExists = true,
                                                userPartOfAgreement = true,
                                                "https://linked.evidence.com")

        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
    }

    @Test
    fun `LinkEvidence failed user does not exist`(){
        //given

        //when
        val response = parameterizeLinkEvidence(userExists = false,
            agreementExists = true,
            userPartOfAgreement = true,
            "https://linked.evidence.com")

        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `LinkEvidence failed agreement does not exist`(){
        //given

        //when
        val response = parameterizeLinkEvidence(userExists = true,
            agreementExists = false,
            userPartOfAgreement = true,
            "https://linked.evidence.com")

        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `LinkEvidence failed user not part of the agreement`(){
        //given

        //when
        val response = parameterizeLinkEvidence(userExists = true,
            agreementExists = true,
            userPartOfAgreement = false,
            "https://linked.evidence.com")

        assertEquals(response.status, ResponseStatus.FAILED)
    }
}