package com.savannasolutions.SmartContractVerifierServer.models
import javax.persistence.*

@Entity
@Table(name = "User")
data class User(@Id val publicWalletID: String,
                val email: String,
                val alias: String,
                @OneToMany var agreements: List<Agreements> )

class MockUser{
    private val emptyAgreements = ArrayList<Agreements>();
    val userA = User("0x684CA5613fE09C0DBb754D929E7a1788464Cd0A6", "UserA@WorkHouse.com", "WorkAddict", emptyAgreements);
    val userB = User("0x7766758C097cb4FB68d0DBEBc1C49AF03d883eBF", "UserB@HouseOfWork.ninja", "IHeartWork", emptyAgreements);
    val userC = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23", "UserC@weWorkForFood.co.za", "WorkForFree", emptyAgreements);
    val userD = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4", "UserD@WorkInc", "IncOfWork",emptyAgreements);
    val userE = User("0x2d19123D26200B30F4687CAFBd8d5E8f0272BdCA", "UserE@workersInc","ThaWorker", emptyAgreements);
}