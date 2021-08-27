package com.savannasolutions.SmartContractVerifierServer.IntegrationTests.JPATests.messenger

import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.contracts.repositories.JudgesRepository
import com.savannasolutions.SmartContractVerifierServer.messenger.models.MessageStatus
import com.savannasolutions.SmartContractVerifierServer.messenger.models.Messages
import com.savannasolutions.SmartContractVerifierServer.messenger.repositories.MessageStatusRepository
import com.savannasolutions.SmartContractVerifierServer.messenger.repositories.MessagesRepository
import com.savannasolutions.SmartContractVerifierServer.messenger.requests.SendMessageRequest
import com.savannasolutions.SmartContractVerifierServer.messenger.services.MessengerService
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa
import org.springframework.boot.test.context.SpringBootTest
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest
@AutoConfigureDataJpa
class SendMessageDatabaseTest {
    @Autowired
    lateinit var messagesRepository: MessagesRepository

    @Autowired
    lateinit var messagesStatusRepository: MessageStatusRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var agreementsRepository: AgreementsRepository

    @Autowired
    lateinit var judgesRepository: JudgesRepository

    lateinit var messagesService: MessengerService
    private lateinit var userA : User
    private lateinit var userB : User
    private lateinit var agreement : Agreements
    private lateinit var messageA : Messages
    private lateinit var messageStatus: List<MessageStatus>

    @BeforeEach
    fun beforeEach()
    {
        messagesService = MessengerService(messagesRepository,
            messagesStatusRepository,
            userRepository,
            agreementsRepository,
            judgesRepository)

        userA = User("0x743Fb032c0bE976e1178d8157f911a9e825d9E23")
        userB = User("0x37Ec9a8aBFa094b24054422564e68B08aF3114B4")
        agreement = Agreements(
            UUID.fromString("5483fed3-6ec4-48d4-8633-9c8ef90ba4d8"),
            "Test title",
            "Test description",
            CreatedDate = Date()
        )

        agreement = agreement.apply { users.add(userA) }.apply { users.add(userB) }
        userRepository.save(userA)
        userRepository.save(userB)
        agreement = agreementsRepository.save(agreement)
        userA = userA.apply { agreements.add(agreement) }
        userB = userB.apply { agreements.add(agreement) }
        userA = userRepository.save(userA)
        userB = userRepository.save(userB)
    }

    @AfterEach
    fun afterEach()
    {
        userA.agreements.remove(agreement)
        userB.agreements.remove(agreement)
        userA = userRepository.save(userA)
        userB = userRepository.save(userB)
        for(msgStat in messageStatus)
            messagesStatusRepository.delete(msgStat)
        messagesRepository.delete(messageA)
        agreement.messages = emptyList()
        agreementsRepository.delete(agreement)
        userRepository.delete(userA)
        userRepository.delete(userB)
    }

    @Test
    fun `SendMessage success`()
    {
        val request = SendMessageRequest(userA.publicWalletID,
                                        agreement.ContractID,
                                        "Test this message")

        val response = messagesService.sendMessage(request)

        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
        assertNotNull(response.MessageID)
        messageA = messagesRepository.getById(response.MessageID!!)
        assertNotNull(messageA)
        assertEquals(messageA.message, request.Message)
        assertEquals(messageA.agreements.ContractID, request.AgreementID)
        assertEquals(messageA.sender.publicWalletID, request.SendingUser)
        assertNotNull(messagesStatusRepository.getAllByMessage(messageA))
        messageStatus = messagesStatusRepository.getAllByMessage(messageA)!!
        for(msgStat in messageStatus)
            assertEquals(msgStat.message, messageA)
    }

}