package com.savannasolutions.SmartContractVerifierServer.services

import com.savannasolutions.SmartContractVerifierServer.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.models.ConditionStatus
import com.savannasolutions.SmartContractVerifierServer.models.Conditions
import com.savannasolutions.SmartContractVerifierServer.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.repositories.ConditionsRepository
import com.savannasolutions.SmartContractVerifierServer.requests.*
import com.savannasolutions.SmartContractVerifierServer.responses.ResponseStatus
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.Duration
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import kotlin.collections.ArrayList

@SpringBootTest
internal class NegotiationServiceTest
    @Autowired
    constructor(private val negotiationService: NegotiationService,
                                                  private val agreementsRepository: AgreementsRepository,
                                                  private val conditionsRepository: ConditionsRepository,
                                                    ){

    private var agreementAUUID = UUID.randomUUID()
    private var agreementBUUID = UUID.randomUUID()
    private var agreementCUUID = UUID.randomUUID()
    private var conditionAUUID = UUID.randomUUID()
    private var conditionBUUID = UUID.randomUUID()
    private var conditionCUUID = UUID.randomUUID()
    private var conditionDUUID = UUID.randomUUID()
    private var conditionEUUID = UUID.randomUUID()
    private var conditionFUUID = UUID.randomUUID()
    private var conditionGUUID = UUID.randomUUID()

    @BeforeEach
    fun setUp() {
        val dateA = Date.from(LocalDate.of(2021,6,1).atStartOfDay(ZoneId.of( "Africa/Tunis" )).toInstant())
        val dateB = Date.from(LocalDate.of(2021,6,2).atStartOfDay(ZoneId.of( "Africa/Tunis" )).toInstant())
        val dateC = Date.from(LocalDate.of(2021,6,3).atStartOfDay(ZoneId.of( "Africa/Tunis" )).toInstant())
        val dateD = Date.from(LocalDate.of(2021,6,4).atStartOfDay(ZoneId.of( "Africa/Tunis" )).toInstant())

        val agreementA = agreementsRepository.save(Agreements(UUID.fromString("2e19610c-a2ce-4444-824a-238028e7d18d"),
                                            UUID.randomUUID().toString(),
                                            "0x684CA5613fE09C0DBb754D929E7a1788464Cd0A6",
                                            "0x7766758C097cb4FB68d0DBEBc1C49AF03d883eBF",
                                            Date(),
                                            null,
                                            null,
                                            false,
                                            null,
                                            null,))

        agreementAUUID = agreementA.ContractID

        val agreementB = agreementsRepository.save(Agreements(UUID.fromString("93f3e25c-6f78-4943-8b8c-48b43e83697d"),
                                            UUID.randomUUID().toString(),
                                    "0x743Fb032c0bE976e1178d8157f911a9e825d9E23",
                                    "0x7766758C097cb4FB68d0DBEBc1C49AF03d883eBF",
                                            dateA,
                                            Date(),
                                            Duration.between(dateA.toInstant(),Date().toInstant()),
                            true,
                                    null,
                                    null,))

        agreementBUUID = agreementB.ContractID

        val agreementC = agreementsRepository.save(Agreements(UUID.fromString("8b8c6b25-7db8-4f87-b869-90e4cd8a246b"),
                UUID.randomUUID().toString(),
                "0x743Fb032c0bE976e1178d8157f911a9e825d9E23",
                "0x7766758C097cb4FB68d0DBEBc1C49AF03d883eBF",
                dateD,
                Date(),
                Duration.between(dateD.toInstant(),Date().toInstant()),
                true,
                null,
                null,))

        agreementCUUID = agreementC.ContractID

        val conditionA = conditionsRepository.save(Conditions(UUID.fromString("6154f307-3c79-4e34-a18b-1d77bb397d98"),
                            "Condition A",
                                            ConditionStatus.ACCEPTED,
                                "0x7766758C097cb4FB68d0DBEBc1C49AF03d883eBF",
                                            dateA,
                                            agreementB))

        conditionAUUID = conditionA.conditionID

        val conditionB = conditionsRepository.save(Conditions(UUID.fromString("8c339f1c-32f5-4075-ad7a-88ba11bceccb"),
                "Condition B",
                ConditionStatus.REJECTED,
                "0x7766758C097cb4FB68d0DBEBc1C49AF03d883eBF",
                dateB,
                agreementB))

        conditionBUUID = conditionB.conditionID

        val conditionC = conditionsRepository.save(Conditions(UUID.fromString("eb890273-e35c-4d60-a30a-e900c20d908c"),
                "Condition C",
                ConditionStatus.REJECTED,
                "0x7766758C097cb4FB68d0DBEBc1C49AF03d883eBF",
                dateC,
                agreementB))

        conditionCUUID = conditionC.conditionID

        val conditionD = conditionsRepository.save(Conditions(UUID.fromString("ee14b856-009a-404b-8fdb-db27c85e0a5b"),
                "Condition D",
                ConditionStatus.PENDING,
                "0x7766758C097cb4FB68d0DBEBc1C49AF03d883eBF",
                dateD,
                agreementC))

        conditionDUUID = conditionD.conditionID

        val conditionE = conditionsRepository.save(Conditions(UUID.fromString("2eb42bdc-c110-4e08-9f83-d3a73c821425"),
                "Condition E",
                ConditionStatus.ACCEPTED,
                "0x7766758C097cb4FB68d0DBEBc1C49AF03d883eBF",
                dateA,
                agreementC))

        conditionEUUID = conditionE.conditionID

        val conditionF = conditionsRepository.save(Conditions(UUID.fromString("881f264c-9d8c-4aac-99df-efbba76077c4"),
                "Condition F",
                ConditionStatus.REJECTED,
                "0x7766758C097cb4FB68d0DBEBc1C49AF03d883eBF",
                dateB,
                agreementC))

        conditionFUUID = conditionF.conditionID

        val conditionG = conditionsRepository.save(Conditions(UUID.fromString("9d0e4444-f24e-449e-8233-66330b2ef8a1"),
                "Condition G",
                ConditionStatus.REJECTED,
                "0x7766758C097cb4FB68d0DBEBc1C49AF03d883eBF",
                dateC,
                agreementC))

        conditionGUUID = conditionG.conditionID

        val conditionListA = ArrayList<Conditions>()
        conditionListA.add(conditionA)
        conditionListA.add(conditionB)
        conditionListA.add(conditionC)

        val conditionListB = ArrayList<Conditions>()
        conditionListB.add(conditionD)
        conditionListB.add(conditionE)
        conditionListB.add(conditionF)
        conditionListB.add(conditionG)

        agreementB.conditions = conditionListA
        agreementsRepository.save(agreementB)

        agreementC.conditions = conditionListB
        agreementsRepository.save(agreementC)
    }

    @AfterEach
    fun tearDown() {
        agreementsRepository.deleteById(agreementAUUID)
        agreementsRepository.deleteById(agreementBUUID)
        agreementsRepository.deleteById(agreementCUUID)
    }

    @Test
    fun acceptCondition() {
        //Failed response
        assertEquals(negotiationService.acceptCondition(AcceptConditionRequest(UUID.randomUUID())).status, ResponseStatus.FAILED)

        //Successful response
        assertEquals(negotiationService.acceptCondition(AcceptConditionRequest(conditionDUUID)).status, ResponseStatus.SUCCESSFUL)
        assertEquals(conditionsRepository.getById(conditionDUUID).conditionStatus, ConditionStatus.ACCEPTED)
    }

    @Test
    fun createAgreement() {
        //Failed Response
        assertEquals(negotiationService.createAgreement(CreateAgreementRequest("","0x684CA5613fE09C0DBb754D929E7a1788464Cd0A6")).status, ResponseStatus.FAILED)
        assertEquals(negotiationService.createAgreement(CreateAgreementRequest("0x684CA5613fE09C0DBb754D929E7a1788464Cd0A6", "")).status, ResponseStatus.FAILED)
        assertEquals(negotiationService.createAgreement(CreateAgreementRequest("0x684CA5613fE09C0DBb754D929E7a1788464Cd0A6", "0x684CA5613fE09C0DBb754D929E7a1788464Cd0A6")).status, ResponseStatus.FAILED)

        //Successful Response
        val response = negotiationService.createAgreement(CreateAgreementRequest("0x7766758C097cb4FB68d0DBEBc1C49AF03d883eBF","0x684CA5613fE09C0DBb754D929E7a1788464Cd0A6"))
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
        assertNotEquals(response.agreementID, null)
        val responseUUID = response.agreementID
        assertNotEquals(responseUUID?.let { agreementsRepository.getById(it) }, null)

    }

    @Test
    fun createCondition() {
        //Failed responses
        assertEquals(negotiationService.createCondition(CreateConditionRequest("0x684CA5613fE09C0DBb754D929E7a1788464Cd0A6",UUID.randomUUID(),"This test should fail")).status,ResponseStatus.FAILED)
        assertEquals(negotiationService.createCondition(CreateConditionRequest("",agreementAUUID,"This test should fail")).status, ResponseStatus.FAILED)
        assertEquals(negotiationService.createCondition(CreateConditionRequest("0x684CA5613fE09C0DBb754D929E7a1788464Cd0A6",agreementAUUID,"")).status,ResponseStatus.FAILED)
        assertEquals(negotiationService.createCondition(CreateConditionRequest("0x743Fb032c0bE976e1178d8157f911a9e825d9E23",agreementAUUID,"")).status,ResponseStatus.FAILED)

        //Successful response
        val response = negotiationService.createCondition(CreateConditionRequest("0x684CA5613fE09C0DBb754D929E7a1788464Cd0A6", agreementAUUID, "This test should succeed"))
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
        assertNotNull(agreementsRepository.getById(agreementAUUID).conditions)
        val responseList = agreementsRepository.getById(agreementAUUID).conditions as List<Conditions>
        val condition = response.conditionID?.let { conditionsRepository.getById(it) }
        assertNotEquals(responseList, null)
        var found = false
        for(cond in responseList)
        {
            if(cond.conditionID == condition?.conditionID)
            {
                found = true
                break
            }
        }
        assertTrue(found)

    }

    @Test
    fun getAgreementDetails() {
        //failed response
        assertEquals(negotiationService.getAgreementDetails(GetAgreementDetailsRequest(UUID.randomUUID())).status, ResponseStatus.FAILED)

        //successful response
        val response = negotiationService.getAgreementDetails(GetAgreementDetailsRequest(agreementBUUID))
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
        assertEquals(response.agreementID, agreementBUUID)
    }

    @Test
    fun rejectCondition() {
        //Failed response
        assertEquals(negotiationService.rejectCondition(RejectConditionRequest(UUID.randomUUID())).status, ResponseStatus.FAILED)

        //Successful response
        assertEquals(negotiationService.rejectCondition(RejectConditionRequest(conditionDUUID)).status, ResponseStatus.SUCCESSFUL)
        assertEquals(conditionsRepository.getById(conditionDUUID).conditionStatus, ConditionStatus.REJECTED)
    }

    @Test
    fun getAllConditions() {
    }

    @Test
    fun sealAgreement() {
    }

    @Test
    fun getConditionDetails(){
        //failed response
        assertEquals(negotiationService.getConditionDetails(GetConditionDetailsRequest(UUID.randomUUID())).status,ResponseStatus.FAILED)

        //successful response
        val response = negotiationService.getConditionDetails(GetConditionDetailsRequest(conditionDUUID))
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
        assertEquals(response.conditionID, conditionDUUID)
    }
}