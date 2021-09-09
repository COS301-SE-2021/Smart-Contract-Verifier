package com.savannasolutions.SmartContractVerifierServer.UnitTests.messenger

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ApiResponse
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.contracts.repositories.JudgesRepository
import com.savannasolutions.SmartContractVerifierServer.messenger.models.MessageStatus
import com.savannasolutions.SmartContractVerifierServer.messenger.models.Messages
import com.savannasolutions.SmartContractVerifierServer.messenger.repositories.MessageStatusRepository
import com.savannasolutions.SmartContractVerifierServer.messenger.repositories.MessagesRepository
import com.savannasolutions.SmartContractVerifierServer.messenger.responses.GetUnreadMessagesResponse
import com.savannasolutions.SmartContractVerifierServer.messenger.services.MessengerService
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
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal class GetUnreadMessagesUnitTest {
    private val messagesRepository: MessagesRepository = mock()
    private val messageStatusRepository : MessageStatusRepository = mock()
    private val userRepository : UserRepository = mock()
    private val agreementsRepository: AgreementsRepository = mock()
    private val judgesRepository : JudgesRepository = mock()
    private val messengerService = MessengerService(messagesRepository,
        messageStatusRepository,
        userRepository,
        agreementsRepository,
        judgesRepository)

    private lateinit var agreementAUUID : UUID
    private lateinit var agreementBUUID : UUID
    private lateinit var userAWalletID: String
    private lateinit var userBWalletID: String
    private lateinit var messageAID: UUID
    private lateinit var messageBID: UUID

    private fun parameterizeGetUnreadMessages(): ApiResponse<GetUnreadMessagesResponse>
    {
        val userA = User("userA")
        userAWalletID = userA.publicWalletID
        val userB = User("userB")
        userBWalletID = userB.publicWalletID

        val agreementA = Agreements(
            ContractID = UUID.fromString("1060e965-ce97-45c9-bece-88ec22b88f86"),
            CreatedDate = Date()).apply { users.add(userA) }.apply { users.add(userB) }
        agreementAUUID = agreementA.ContractID

        val agreementB = Agreements(
            ContractID = UUID.fromString("432cb7d7-a9c5-468c-8c7a-6cc0634b041a"),
            CreatedDate = Date()).apply { users.add(userB) }.apply { users.add(userA) }
        agreementBUUID = agreementB.ContractID

        val messageA = Messages(UUID.fromString("0291184c-036a-457c-a37f-1684c69bd9bb"),
                                "Test message", Date()).apply { sender = userA }.apply { agreements = agreementA }

        val messageB = Messages(UUID.fromString("4718af9e-d0f6-4677-a639-79a6fe6cff7f"),
                                "Test message 2", Date()).apply { sender = userB }.apply { agreements = agreementB }

        messageAID = messageA.messageID
        messageBID = messageB.messageID

        val messageStatusA =
            MessageStatus(UUID.fromString("8c5c8aa5-eac6-4dfe-90a2-d0be8b6005a3")).apply { recipient = userB }.apply { message = messageA }

        val messageStatusB =
            MessageStatus(UUID.fromString("f1526d67-a0af-4c08-bca2-6285d631c0a5"),
                Date()).apply { recipient = userA }.apply { message = messageB }

        val agreementSet = mutableSetOf<Agreements>()
        agreementSet.add(agreementA)
        agreementSet.add(agreementB)

        val messageList = ArrayList<Messages>()
        messageList.add(messageA)
        messageList.add(messageB)

        //val messageAStatusDone = messageStatusA
        //messageAStatusDone.ReadDate = Date()

        val messageStatusList = ArrayList<MessageStatus>()
        messageStatusList.add(messageStatusA)

        //when
        whenever(userRepository.getById(userAWalletID)).thenReturn(userA)
        whenever(userRepository.getById(userBWalletID)).thenReturn(userB)
        whenever(agreementsRepository.getById(agreementAUUID)).thenReturn(agreementA)
        whenever(agreementsRepository.getById(agreementBUUID)).thenReturn(agreementB)
        whenever(agreementsRepository.getAllByUsersContaining(userB)).thenReturn(agreementSet)
        whenever(messagesRepository.getAllByAgreements(agreementA)).thenReturn(messageList)
        whenever(messageStatusRepository.getByRecipientAndMessage(userB, messageA)).thenReturn(messageStatusA)
        whenever(messageStatusRepository.getByRecipientAndMessage(userB, messageB)).thenReturn(messageStatusB)
        //whenever(messageStatusRepository.save(messageStatusA)).thenReturn(messageAStatusDone)
        whenever(messageStatusRepository.getAllByMessage(messageA)).thenReturn(messageStatusList)

        //then
        return messengerService.getUnreadMessages(userBWalletID)
    }

    @Test
    fun `GetUnreadMessages successful`()
    {
        //given

        //when
        val response = parameterizeGetUnreadMessages()

        //then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
        assertNotNull(response.responseObject)
        assertNotNull(response.responseObject!!.MessageAgreementList)
        assertFalse(response.responseObject!!.MessageAgreementList!!.isEmpty())
        assertEquals(response.responseObject!!.MessageAgreementList!![0].AgreementID, agreementAUUID)
        assertNotNull(response.responseObject!!.MessageAgreementList!![0].Messages)
        assertEquals(response.responseObject!!.MessageAgreementList!![0].Messages!![0].MessageID, messageAID)
        assertNotNull(response.responseObject!!.MessageAgreementList!![0].Messages!![0].MessageStatuses)
        assertNotNull(response.responseObject!!.MessageAgreementList!![0].Messages!![0].MessageStatuses[0].ReadDate)
        assertTrue(response.responseObject!!.MessageAgreementList!![0].Messages!![0].MessageStatuses[0].Read)

    }
}