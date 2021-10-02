package com.savannasolutions.SmartContractVerifierServer.negotiation.repositories

import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.math.BigInteger
import java.util.*

@Repository
interface AgreementsRepository : JpaRepository<Agreements, UUID>{
    fun getAllByUsersContaining(user: User): MutableSet<Agreements>?
    fun getAgreementsByBlockchainID(index: BigInteger): Agreements?
    fun countAgreements(): Int
    fun countAgreementsByBlockchainIDNotNull(): Int
    fun countAgreementsByBlockchainIDNull(): Int
    fun getAllBySealedDateNotNull(): List<Agreements>?= emptyList()
    fun countAgreementsByCreatedDateBetween(beginDate: Date, endDate:Date): Int
    fun countAgreementsBySealedDateBetween(beginDate: Date, endDate: Date): Int
    fun countAgreementsByCreatedDateBefore(date: Date): Int
    fun countAgreementsBySealedDateBefore(date: Date): Int
}