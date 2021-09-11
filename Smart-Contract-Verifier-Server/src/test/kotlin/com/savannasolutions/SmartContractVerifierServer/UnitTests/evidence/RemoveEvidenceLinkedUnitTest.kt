package com.savannasolutions.SmartContractVerifierServer.UnitTests.evidence

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ApiResponse
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.contracts.repositories.JudgesRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.configuration.EvidenceConfig
import com.savannasolutions.SmartContractVerifierServer.evidence.models.Evidence
import com.savannasolutions.SmartContractVerifierServer.evidence.models.EvidenceType
import com.savannasolutions.SmartContractVerifierServer.evidence.models.LinkedEvidence
import com.savannasolutions.SmartContractVerifierServer.evidence.repositories.EvidenceRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.repositories.LinkedEvidenceRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.repositories.UploadedEvidenceRepository
import com.savannasolutions.SmartContractVerifierServer.evidence.services.EvidenceService
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.*
import kotlin.collections.ArrayList
import kotlin.test.assertEquals

internal class RemoveEvidenceLinkedUnitTest {
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

    private var linkedEvidence: Evidence? = null

    private fun parameterizedRemoveEvidenceUploadedUnitTest(userExists: Boolean,
                                                            agreementExists: Boolean,
                                                            userPartOfAgreement: Boolean,
                                                            evidenceExist : Boolean): ApiResponse<Objects>
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

        linkedEvidence = Evidence(UUID.fromString("7d793c67-10e8-419b-8137-be9758594184"),"linked evidence", EvidenceType.LINKED)
        linkedEvidence!!.user = user
        linkedEvidence!!.contract = agreement

        val linkedEvidenceObj = LinkedEvidence(UUID.fromString("523ce05d-aea1-42b4-a405-2ed345e8ecb6"),
            "an url")
        linkedEvidenceObj.evidence = linkedEvidence!!
        linkedEvidence!!.evidenceUrl = linkedEvidenceObj

        val evidenceList = ArrayList<Evidence>()
        evidenceList.add(linkedEvidence!!)

        //when
        whenever(agreementsRepository.existsById(agreement.ContractID)).thenReturn(agreementExists)
        whenever(agreementsRepository.getById(agreement.ContractID)).thenReturn(agreement)
        whenever(userRepository.getById(user.publicWalletID)).thenReturn(user)
        whenever(userRepository.getById(otherUser.publicWalletID)).thenReturn(otherUser)
        whenever(userRepository.existsById(user.publicWalletID)).thenReturn(userExists)
        whenever(userRepository.existsById(otherUser.publicWalletID)).thenReturn(true)
        whenever(evidenceRepository.existsById(linkedEvidence!!.evidenceHash)).thenReturn(evidenceExist)
        whenever(evidenceRepository.getById(linkedEvidence!!.evidenceHash)).thenReturn(linkedEvidence)
        whenever(userRepository.getUsersByAgreementsContaining(agreement)).thenReturn(agreement.users.toList())
        whenever(evidenceRepository.getAllByContract(agreement)).thenReturn(evidenceList)

        //then
        return if(userPartOfAgreement){
            evidenceService.removeEvidence(user.publicWalletID, agreement.ContractID, linkedEvidence!!.evidenceHash)
        } else
            evidenceService.removeEvidence(otherUser.publicWalletID, agreement.ContractID, linkedEvidence!!.evidenceHash)

    }


    @Test
    fun `RemoveEvidenceLinkedUnitTest successful`()
    {
        //given

        //when
        val response = parameterizedRemoveEvidenceUploadedUnitTest(userExists = true,
            agreementExists = true,
            userPartOfAgreement = true,
            evidenceExist = true,)

        //then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
    }

    @Test
    fun `RemoveEvidenceLinkedUnitTest failed user does not exist`()
    {
        //given

        //when
        val response = parameterizedRemoveEvidenceUploadedUnitTest(userExists = false,
            agreementExists = true,
            userPartOfAgreement = true,
            evidenceExist = false,)

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `RemoveEvidenceLinkedUnitTest failed agreement does not exist`()
    {
        //given

        //when
        val response = parameterizedRemoveEvidenceUploadedUnitTest(userExists = true,
            agreementExists = false,
            userPartOfAgreement = true,
            evidenceExist = true,)

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `RemoveEvidenceLinkedUnitTest failed user not part of agreement`()
    {
        //given

        //when
        val response = parameterizedRemoveEvidenceUploadedUnitTest(userExists = false,
            agreementExists = true,
            userPartOfAgreement = false,
            evidenceExist = true,)

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `RemoveEvidenceLinkedUnitTest failed evidence does not exist`()
    {
        //given

        //when
        val response = parameterizedRemoveEvidenceUploadedUnitTest(userExists = false,
            agreementExists = true,
            userPartOfAgreement = false,
            evidenceExist = false,)

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

}