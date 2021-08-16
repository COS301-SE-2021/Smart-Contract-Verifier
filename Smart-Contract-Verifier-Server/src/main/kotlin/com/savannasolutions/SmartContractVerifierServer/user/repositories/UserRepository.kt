package com.savannasolutions.SmartContractVerifierServer.user.repositories

import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, String>{
    fun getUsersByAgreementsContaining(agreements: Agreements): List<User>
    fun getUserByPublicWalletIDAllIgnoreCase(id: String): User?
    fun existsByPublicWalletIDAllIgnoreCase(id: String): Boolean
}