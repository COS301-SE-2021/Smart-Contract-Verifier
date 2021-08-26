package com.savannasolutions.SmartContractVerifierServer.IntegrationTests.JPATests.messenger

import com.savannasolutions.SmartContractVerifierServer.common.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.contracts.repositories.JudgesRepository
import com.savannasolutions.SmartContractVerifierServer.messenger.models.MessageStatus
import com.savannasolutions.SmartContractVerifierServer.messenger.models.Messages
import com.savannasolutions.SmartContractVerifierServer.messenger.repositories.MessageStatusRepository
import com.savannasolutions.SmartContractVerifierServer.messenger.repositories.MessagesRepository
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
class GetAllMessagesByUserDatabaseTest {
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
    private lateinit var messageB : Messages
    private lateinit var messageStatusA: MessageStatus
    private lateinit var messageStatusB: MessageStatus

    @BeforeEach
    fun beforeEach()
    {
        messagesService = MessengerService(messagesRepository,
            messagesStatusRepository,
            userRepository,
            agreementsRepository, judgesRepository)

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

        messageA = Messages(
            UUID.fromString("e47456d6-4f19-4fba-af22-e80e89d715bd"),
            "Message A",
            Date()
        ).apply { sender = userA }.apply { agreements = agreement }
        messageA = messagesRepository.save(messageA)
        messageStatusA = MessageStatus(UUID.fromString("f24603a6-1ac0-4c95-9bd7-13d67fe0544e")).apply { recipient = userB }.apply { message = messageA }
        messageStatusA = messagesStatusRepository.save(messageStatusA)

        messageB = Messages(
            UUID.fromString("548aa4a6-0287-4d34-88e1-9ae08b8a9d0a"),
            "Message B",
            Date()
        ).apply { sender = userA }.apply { agreements = agreement }
        messageB = messagesRepository.save(messageB)
        messageStatusB = MessageStatus(UUID.fromString("5f397e95-cef2-472d-b1cf-fa820c8a27ea")).apply { recipient = userB }.apply { message = messageB }
        messageStatusB = messagesStatusRepository.save(messageStatusB)
    }

    @AfterEach
    fun afterEach()
    {
        userA.agreements.remove(agreement)
        userB.agreements.remove(agreement)
        userA = userRepository.save(userA)
        userB = userRepository.save(userB)
        messagesStatusRepository.delete(messageStatusA)
        messagesStatusRepository.delete(messageStatusB)
        messagesRepository.delete(messageA)
        messagesRepository.delete(messageB)
        agreement.messages = emptyList()
        agreementsRepository.delete(agreement)
        userRepository.delete(userA)
        userRepository.delete(userB)
    }

    @Test
    fun `GetAllMessagesByUser successful`()
    {
        val response = messagesService.getAllMessagesByUser(userA.publicWalletID)

        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
        assertNotNull(response.messages)
        val msg1 = response.messages!![0]
        val msg2 = response.messages!![1]

        assertEquals(msg1.AgreementID, messageA.agreements.ContractID)
        assertEquals(msg1.Message, messageA.message)
        assertEquals(msg1.MessageID, messageA.messageID)


        assertEquals(msg2.AgreementID, messageB.agreements.ContractID)
        assertEquals(msg2.Message, messageB.message)
        assertEquals(msg2.MessageID, messageB.messageID)
    }
}