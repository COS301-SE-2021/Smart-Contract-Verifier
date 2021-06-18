package com.savannasolutions.SmartContractVerifierServer.services

import com.savannasolutions.SmartContractVerifierServer.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.models.ConditionStatus
import com.savannasolutions.SmartContractVerifierServer.models.Conditions
import com.savannasolutions.SmartContractVerifierServer.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.repositories.ConditionsRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.Instant
import java.util.*
import kotlin.collections.ArrayList


internal class NegotiationServiceTest constructor(private val agreementsRepository: AgreementsRepository,
                                                    private val conditionsRepository: ConditionsRepository,
                                                    ){

    @BeforeEach
    fun setUp() {
        agreementsRepository.save(Agreements(UUID.fromString("2e19610c-a2ce-4444-824a-238028e7d18d"),
                                            UUID.randomUUID().toString(),
                                            "0x684CA5613fE09C0DBb754D929E7a1788464Cd0A6",
                                            "0x7766758C097cb4FB68d0DBEBc1C49AF03d883eBF",
                                            Date(),
                                            null,
                                            null,
                                            false,
                                            null,
                                            null,))

        val agreementB = agreementsRepository.save(Agreements(UUID.fromString("93f3e25c-6f78-4943-8b8c-48b43e83697d"),
                                            UUID.randomUUID().toString(),
                                    "0x743Fb032c0bE976e1178d8157f911a9e825d9E23",
                                    "0x7766758C097cb4FB68d0DBEBc1C49AF03d883eBF",
                                            Date.from(Instant.parse("2021-06-01")),
                                            Date(),
                                            Duration.between(Instant.parse("2021-06-01"),Instant.parse((Date().toString()))),
                            true,
                                    null,
                                    null,))

        val agreementC = agreementsRepository.save(Agreements(UUID.fromString("8b8c6b25-7db8-4f87-b869-90e4cd8a246b"),
                UUID.randomUUID().toString(),
                "0x743Fb032c0bE976e1178d8157f911a9e825d9E23",
                "0x7766758C097cb4FB68d0DBEBc1C49AF03d883eBF",
                Date.from(Instant.parse("2021-06-01")),
                Date(),
                Duration.between(Instant.parse("2021-06-01"),Instant.parse((Date().toString()))),
                true,
                null,
                null,))

        val conditionA = conditionsRepository.save(Conditions(UUID.fromString("6154f307-3c79-4e34-a18b-1d77bb397d98"),
                            "Condition A",
                                            ConditionStatus.ACCEPTED,
                                "0x7766758C097cb4FB68d0DBEBc1C49AF03d883eBF",
                                Date.from(Instant.parse("2021-06-01")),
                                    agreementB))

        val conditionB = conditionsRepository.save(Conditions(UUID.fromString("8c339f1c-32f5-4075-ad7a-88ba11bceccb"),
                "Condition B",
                ConditionStatus.REJECTED,
                "0x7766758C097cb4FB68d0DBEBc1C49AF03d883eBF",
                Date.from(Instant.parse("2021-06-02")),
                agreementB))

        val conditionC = conditionsRepository.save(Conditions(UUID.fromString("eb890273-e35c-4d60-a30a-e900c20d908c"),
                "Condition C",
                ConditionStatus.REJECTED,
                "0x7766758C097cb4FB68d0DBEBc1C49AF03d883eBF",
                Date.from(Instant.parse("2021-06-03")),
                agreementB))

        val conditionD = conditionsRepository.save(Conditions(UUID.fromString("ee14b856-009a-404b-8fdb-db27c85e0a5b"),
                "Condition D",
                ConditionStatus.PENDING,
                "0x7766758C097cb4FB68d0DBEBc1C49AF03d883eBF",
                Date.from(Instant.parse("2021-06-04")),
                agreementC))

        val conditionE = conditionsRepository.save(Conditions(UUID.fromString("2eb42bdc-c110-4e08-9f83-d3a73c821425"),
                "Condition E",
                ConditionStatus.ACCEPTED,
                "0x7766758C097cb4FB68d0DBEBc1C49AF03d883eBF",
                Date.from(Instant.parse("2021-06-01")),
                agreementC))

        val conditionF = conditionsRepository.save(Conditions(UUID.fromString("881f264c-9d8c-4aac-99df-efbba76077c4"),
                "Condition F",
                ConditionStatus.REJECTED,
                "0x7766758C097cb4FB68d0DBEBc1C49AF03d883eBF",
                Date.from(Instant.parse("2021-06-02")),
                agreementC))

        val conditionG = conditionsRepository.save(Conditions(UUID.fromString("9d0e4444-f24e-449e-8233-66330b2ef8a1"),
                "Condition G",
                ConditionStatus.REJECTED,
                "0x7766758C097cb4FB68d0DBEBc1C49AF03d883eBF",
                Date.from(Instant.parse("2021-06-03")),
                agreementC))

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
    }

    @Test
    fun acceptCondition() {
    }

    @Test
    fun createAgreement() {
    }

    @Test
    fun createCondition() {
    }

    @Test
    fun getAgreementDetails() {
    }

    @Test
    fun rejectCondition() {
    }

    @Test
    fun getAllConditions() {
    }

    @Test
    fun sealAgreement() {
    }

    @Test
    fun getAgreementsRepository() {
    }

    @Test
    fun getConditionsRepository() {
    }

    @Test
    fun getUserRepository() {
    }
}