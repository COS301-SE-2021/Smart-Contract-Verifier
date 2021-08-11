import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:unison/models/global.dart';
import 'package:unison/models/message.dart';
import 'package:unison/services/Server/messageService.dart';
import 'package:unison/widgets/agreement_messages_panel.dart';

class MessagingScreen extends StatefulWidget {
  static const routeName = '/messaging';

  @override
  _MessagingScreenState createState() => _MessagingScreenState();
}

class _MessagingScreenState extends State<MessagingScreen> {
  MessageService messageService = MessageService();
  List<Message> messages = [];
  Future<List<Message>> _loadMessages(String agreementId) async {
    messages = await messageService.getMessages(agreementId);
    return messages;
  }

  bool _isLoading = false;
  @override
  Widget build(BuildContext context) {
    final agreementId = ModalRoute.of(context).settings.arguments as String;
    bool _isLoading = true;
    try {
      _loadMessages(agreementId);
    } catch (error) {
      showDialog(
          context: context,
          builder: (_) => new AlertDialog(
                title: new Text('Could not fetch Messages'),
                // content: new Text(contractCondition.description),
                actions: <Widget>[
                  TextButton(
                    child: Text('Close'),
                    onPressed: () {
                      Navigator.of(context).pop();
                    },
                  )
                ],
              ));
    }
    _isLoading = false;
    print(messages);

    // final List<Message> messages = [];
    // messages.add(
    //   Message.fromJSON(
    //     {
    //       'sender': '0xf808ee4efb1b80ebd803f02b8f99fcc4a5a65709',
    //       'timeSent': '1111111111111',
    //       'messageText': 'This message was sent first.\nSent by PartyA.',
    //     },
    //   ),
    // );
    // messages.add(
    //   Message.fromJSON(
    //     {
    //       'sender': '0xf999ee4efb1b80ebd803f02b8f99fcc4a5a65709',
    //       'timeSent': '1111111111112',
    //       'messageText': 'This message was the second one sent.\nSent by '
    //           'PartyB.',
    //     },
    //   ),
    // );
    // messages.add(
    //   Message.fromJSON(
    //     {
    //       'sender': '0xf999ee4efb1b80ebd803f02b8f99fcc4a5a65709',
    //       'timeSent': '1111111111122',
    //       'messageText': 'This message was the third one sent.\nSent by '
    //           'PartyB.',
    //     },
    //   ),
    // );
    return Scaffold(
      appBar: AppBar(
        title: Text('Chat for agreement: ' + agreementId),
      ),
      body: Column(
        children: [
          Expanded(
            flex: 6,
            child: AgreementMessagesPanel(messages),
            // padding: EdgeInsets.symmetric(
            //   vertical: 10,
            //   horizontal: 8,
            // ),
            //
          ),
          Expanded(
            flex: 1,
            child: Container(
                // TODO: More Information or actions here?
                ),
          )
        ],
      ),
    );
  }
}
