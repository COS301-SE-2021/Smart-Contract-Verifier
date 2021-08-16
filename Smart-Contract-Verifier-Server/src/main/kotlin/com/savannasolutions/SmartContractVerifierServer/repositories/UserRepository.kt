package com.savannasolutions.SmartContractVerifierServer.repositories

import com.savannasolutions.SmartContractVerifierServer.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, String> {
}