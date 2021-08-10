import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:unison/providers/global.dart';
import 'package:unison/providers/message.dart';

class MessagingScreen extends StatefulWidget {
  static const routeName = '/messaging';

  @override
  _MessagingScreenState createState() => _MessagingScreenState();
}

class _MessagingScreenState extends State<MessagingScreen> {
  @override
  Widget build(BuildContext context) {
    final agreementId = ModalRoute.of(context).settings.arguments as String;
    //Get Messages:
    /*
    chatMessages = GetAllMessagesByAgreementRequest(agreementId, Global
    .userAddress)
    ---- Assuming messages array ----
    [
      {
        'senderId' : '0xf808ee4efb1b80ebd803f02b8f99fcc4a5a65709',
        'timeSent' : '1111111111111',
        'message'  : 'This message was sent first.\nSent by PartyA.',
      },
      {
        'senderId' : '0xf999ee4efb1b80ebd803f02b8f99fcc4a5a65709',
        'timeSent' : '1111111111112',
        'message'  : 'This message was the second one sent.\nSent by PartyB.',
      },
      {
        'senderId' : '0xf999ee4efb1b80ebd803f02b8f99fcc4a5a65709',
        'timeSent' : '1111111111122',
        'message'  : 'This message was the third one sent.\nSent by PartyB.',
      },
    ]

     */

    final List<Message> messages = [];
    messages.add(
      Message.fromJSON(
        {
          'sender': '0xf808ee4efb1b80ebd803f02b8f99fcc4a5a65709',
          'timeSent': '1111111111111',
          'messageText': 'This message was sent first.\nSent by PartyA.',
        },
      ),
    );
    messages.add(
      Message.fromJSON(
        {
          'sender': '0xf999ee4efb1b80ebd803f02b8f99fcc4a5a65709',
          'timeSent': '1111111111112',
          'messageText': 'This message was the second one sent.\nSent by '
              'PartyB.',
        },
      ),
    );
    messages.add(
      Message.fromJSON(
        {
          'sender': '0xf999ee4efb1b80ebd803f02b8f99fcc4a5a65709',
          'timeSent': '1111111111122',
          'messageText': 'This message was the third one sent.\nSent by '
              'PartyB.',
        },
      ),
    );
    return Scaffold(
      body: Text('Chat for Agreement: ${agreementId} Goes Here: \n Messages: '
          '${messages[0].messageText}-${messages[0].sender}'),
    );
  }
}
