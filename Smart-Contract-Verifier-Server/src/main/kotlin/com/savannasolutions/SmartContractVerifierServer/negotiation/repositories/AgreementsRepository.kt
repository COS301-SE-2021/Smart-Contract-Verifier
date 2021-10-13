package com.savannasolutions.SmartContractVerifierServer.negotiation.repositories

import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.math.BigInteger
import java.util.*
import kotlin.collections.ArrayList

@Repository
interface AgreementsRepository : JpaRepository<Agreements, UUID>{
    fun getAllByUsersContaining(user: User): MutableSet<Agreements>?
    fun getAgreementsByBlockchainID(index: BigInteger): Agreements?
    //fun countAgreements(): Int
    fun countAgreementsByBlockchainIDNotNull(): Int
    fun countAgreementsByBlockchainIDNull(): Int

    @Query("SELECT a FROM Agreements a")
    fun selectAll(): List<Agreements> ?= emptyList()

    @Query("SELECT a FROM Agreements a WHERE (a.CreatedDate >= ?1 AND a.CreatedDate <= ?2) OR " +
            "(a.SealedDate >= ?1 AND a.SealedDate <= ?2)")
    fun selectAllBetweenDates(fromDate: Date, toDate: Date) : List<Agreements> ?= emptyList()

    @Query("SELECT a FROM Agreements a WHERE (a.CreatedDate >= ?1 AND a.CreatedDate <= ?2)")
    fun selectCreatedDateBetween(fromDate: Date, toDate: Date) : List<Agreements> ?= emptyList()

    @Query("SELECT a FROM Agreements a WHERE (a.CreatedDate < ?1)")
    fun selectCreatedDateBefore(fromDate: Date) : List<Agreements> ?= emptyList()

    @Query("SELECT a FROM Agreements a WHERE (a.SealedDate >= ?1 AND a.SealedDate <= ?2)")
    fun selectSealedDateBetween(fromDate: Date, toDate: Date) : List<Agreements> ?= emptyList()

    @Query("SELECT a FROM Agreements a WHERE (a.SealedDate < ?1)")
    fun selectSealedDateBefore(fromDate: Date) : List<Agreements> ?= emptyList()
}