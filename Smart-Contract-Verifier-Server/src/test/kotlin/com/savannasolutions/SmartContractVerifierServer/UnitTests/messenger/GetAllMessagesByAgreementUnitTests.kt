package com.savannasolutions.SmartContractVerifierServer.UnitTests.messenger

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ApiResponse
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.contracts.repositories.JudgesRepository
import com.savannasolutions.SmartContractVerifierServer.messenger.models.MessageStatus
import com.savannasolutions.SmartContractVerifierServer.messenger.models.Messages
import com.savannasolutions.SmartContractVerifierServer.messenger.repositories.MessageStatusRepository
import com.savannasolutions.SmartContractVerifierServer.messenger.repositories.MessagesRepository
import com.savannasolutions.SmartContractVerifierServer.messenger.responses.GetAllMessagesByAgreementResponse
import com.savannasolutions.SmartContractVerifierServer.messenger.services.MessengerService
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.*
import kotlin.collections.ArrayList
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal class GetAllMessagesByAgreementUnitTests {
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

    private fun parameterizeGetAllMessagesByAgreement(userAddress: String,
                                                      agreementID: UUID,
                                                      userExist: Boolean,
                                                      agreementExist: Boolean,
                                                      messageList: List<Messages>? = null): ApiResponse<GetAllMessagesByAgreementResponse>
    {
        //given
        val user = User(userAddress)
        val otherUser = User("Other user")
        var agreement = Agreements(ContractID = agreementID,
            CreatedDate = Date(),
            MovedToBlockChain = false,)
        agreement = agreement.apply { users.add(user)}
        agreement = agreement.apply { users.add(otherUser)}

        //when

        whenever(userRepository.existsById(userAddress)).thenReturn(userExist)
        whenever(agreementsRepository.existsById(agreementID)).thenReturn(agreementExist)
        whenever(agreementsRepository.getById(agreementID)).thenReturn(agreement)
        whenever(messagesRepository.getAllByAgreements(agreement)).thenReturn(messageList)
        if(messageList != null)
        {
            for(msg in messageList)
            {
                var msgStatus = MessageStatus(UUID.randomUUID(), Date())
                msgStatus = msgStatus.apply { message = msg }
                if(msg.sender == user)
                    msgStatus = msgStatus.apply { recipient = otherUser }
                else
                    msgStatus = msgStatus.apply { recipient = user }
                val msgStatusList = ArrayList<MessageStatus>()
                msgStatusList.add(msgStatus)
                whenever(messageStatusRepository.getAllByMessage(msg)).thenReturn(msgStatusList)
            }
        }

        //then
        return messengerService.getAllMessagesByAgreement(userAddress, agreementID)
    }

    @Test
    fun `Successful test for getAllMessagesByAgreement without messages`(){
        //given

        //when
        val response = parameterizeGetAllMessagesByAgreement("userA",
            UUID.fromString("7b67f0f4-6433-4a72-b467-c6ddb9dd772a"),
            userExist = true,
            agreementExist = true,)

        //then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
        assertNotNull(response.responseObject)
        assertNotNull(response.responseObject!!.messages)
        assertTrue { response.responseObject!!.messages!!.isEmpty()}
    }

    @Test
    fun `Successful test for getAllMessagesByAgreement with messages`(){
        //given
        val agreement = Agreements(ContractID = UUID.fromString("7b67f0f4-6433-4a72-b467-c6ddb9dd772a"),
            CreatedDate = Date(),
            MovedToBlockChain = false,)

        val messageList = ArrayList<Messages>()
        var msg1 = Messages(UUID.fromString("3d63ca50-5584-460a-881a-24c215f0a5e2"),
            "Message 1", Date())
        msg1 = msg1.apply { sender = User("7b67f0f4-6433-4a72-b467-c6ddb9dd772a") }
        msg1 = msg1.apply { agreements = agreement }

        var msg2 = Messages(UUID.fromString("013e7a05-c831-400c-a43d-507018db7b2c"),
            "Message 2", Date())
        msg2 = msg2.apply { sender = User("7b67f0f4-6433-4a72-b467-c6ddb9dd772a") }
        msg2 = msg2.apply { agreements = agreement }

        messageList.add(msg1)
        messageList.add(msg2)

        //when
        val response = parameterizeGetAllMessagesByAgreement("userA",
            UUID.fromString("7b67f0f4-6433-4a72-b467-c6ddb9dd772a"),
            userExist = true,
            agreementExist = true,
            messageList)

        //then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
        assertNotNull(response.responseObject)
        assertNotNull(response.responseObject!!.messages)
        assertFalse{response.responseObject!!.messages!!.isEmpty()}
    }

    @Test
    fun `getAllMessagesByAgreement failed user does not exist`(){
        //given

        //when
        val response = parameterizeGetAllMessagesByAgreement("userA",
            UUID.fromString("7b67f0f4-6433-4a72-b467-c6ddb9dd772a"),
            userExist = false,
            agreementExist = true)

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

    @Test
    fun `getAllMessagesByAgreement failed agreement does not exist`(){
        //given

        //when
        val response = parameterizeGetAllMessagesByAgreement("userA",
            UUID.fromString("7b67f0f4-6433-4a72-b467-c6ddb9dd772a"),
            userExist = true,
            agreementExist = false)

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

}