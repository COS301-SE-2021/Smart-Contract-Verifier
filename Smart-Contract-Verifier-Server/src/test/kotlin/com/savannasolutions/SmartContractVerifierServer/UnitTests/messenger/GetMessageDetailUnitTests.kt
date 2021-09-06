package com.savannasolutions.SmartContractVerifierServer.UnitTests.messenger

import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ApiResponse
import com.savannasolutions.SmartContractVerifierServer.common.commonDataObjects.ResponseStatus
import com.savannasolutions.SmartContractVerifierServer.contracts.repositories.JudgesRepository
import com.savannasolutions.SmartContractVerifierServer.messenger.models.Messages
import com.savannasolutions.SmartContractVerifierServer.messenger.repositories.MessageStatusRepository
import com.savannasolutions.SmartContractVerifierServer.messenger.repositories.MessagesRepository
import com.savannasolutions.SmartContractVerifierServer.messenger.responses.GetMessageDetailResponse
import com.savannasolutions.SmartContractVerifierServer.messenger.services.MessengerService
import com.savannasolutions.SmartContractVerifierServer.negotiation.models.Agreements
import com.savannasolutions.SmartContractVerifierServer.negotiation.repositories.AgreementsRepository
import com.savannasolutions.SmartContractVerifierServer.user.models.User
import com.savannasolutions.SmartContractVerifierServer.user.repositories.UserRepository
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.*
import kotlin.test.assertEquals

internal class GetMessageDetailUnitTests {
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

    private fun parameterizeGetMessageDetail(messageID: UUID,
                                             messageExist: Boolean): ApiResponse<GetMessageDetailResponse>
    {
        //given
        var message = Messages(messageID,"Test Data", Date())

        val agreement = Agreements(ContractID = UUID.fromString("7b67f0f4-6433-4a72-b467-c6ddb9dd772a"),
            CreatedDate = Date(),
            MovedToBlockChain = false,)

        message = message.apply { agreements = agreement }
        message = message.apply { sender = User("user A") }

        //when
        whenever(messagesRepository.existsById(messageID)).thenReturn(messageExist)
        whenever(messagesRepository.getById(messageID)).thenReturn(message)

        //then
        return messengerService.getMessageDetail(message.sender.publicWalletID, messageID)
    }

    @Test
    fun `getMessageDetail success`(){
        //given

        //when
        val response = parameterizeGetMessageDetail(UUID.fromString("887372ab-d2e9-45a7-af1f-d2a6924c1466"), true)

        //then
        assertEquals(response.status, ResponseStatus.SUCCESSFUL)
    }

    @Test
    fun `getMessageDetail failure message does not exist`(){
        //given

        //when
        val response = parameterizeGetMessageDetail(UUID.fromString("887372ab-d2e9-45a7-af1f-d2a6924c1466"), false)

        //then
        assertEquals(response.status, ResponseStatus.FAILED)
    }

}