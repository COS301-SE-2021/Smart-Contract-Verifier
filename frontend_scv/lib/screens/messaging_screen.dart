import 'package:flutter/material.dart';
import 'package:unison/models/global.dart';
import 'package:unison/services/Server/messageService.dart';
import 'package:unison/widgets/messaging/message_input_panel.dart';
import 'package:unison/widgets/messaging/messages_panel.dart';
import 'package:unison/widgets/miscellaneous/funky_text_widget.dart';

class MessagingScreen extends StatefulWidget {
  @override
  _MessagingScreenState createState() => _MessagingScreenState();
  static const routeName = '/messaging-screen';
}

class _MessagingScreenState extends State<MessagingScreen> {
  void initState() {
    super.initState();
  }

  MessageService messageService = MessageService();
  @override
  Widget build(BuildContext context) {
    print('Message Screen Build: Curr: ${Global.userAddress}');

    final args = ModalRoute.of(context).settings.arguments as Map;
    final agreementId = args['agreementId'];
    final partyA = args['partyA'];
    final partyB = args['partyB'];

    print('PA: $partyA\nPB: $partyB');

    return Scaffold(
      appBar: AppBar(
        title: FunkyText('Agreement Chat: ' + agreementId),
      ),
      // drawer: AppDrawer(),
      body: Column(
        children: [
          Card(
            color: Color.fromRGBO(43, 45, 60, 0.8),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: [
                Text('Participants:'),
                Text(
                  'You: ' +
                      Global.userAddress.substring(0, 6) +
                      '...' +
                      Global.userAddress.substring(
                          Global.userAddress.length - 4,
                          Global.userAddress.length),
                  style: TextStyle(
                    color: Global.userAddress == partyA ||
                            Global.userAddress == partyB
                        ? Colors.pink[400]
                        : Colors.teal[400],
                    // fontSize: 10,
                  ),
                ),
                if (Global.userAddress != partyA &&
                    Global.userAddress == partyB)
                  Text(
                    'Other Party: ' +
                        partyA.substring(0, 6) +
                        '...' +
                        partyA.substring(partyA.length - 4, partyA.length),
                    style: TextStyle(
                      color: Colors.cyan[400],
                      // fontSize: 10,
                    ),
                  ),
                if (Global.userAddress == partyA &&
                    Global.userAddress != partyB)
                  Text(
                    'Other Party: ' +
                        partyB.substring(0, 6) +
                        '...' +
                        partyB.substring(partyB.length - 4, partyB.length),
                    style: TextStyle(
                      color: Colors.cyan[400],
                      // fontSize: 10,
                    ),
                  ),
                Text(
                  'Jurors (when agreement is in dispute): ' +
                      '0x12345'
                          '...6789',
                  style: TextStyle(
                    color: Colors.teal[400],
                    // fontSize: 10,
                  ),
                ),
              ],
            ),
          ),
          Expanded(
            flex: 6,
            child: Padding(
              padding: EdgeInsets.symmetric(
                      vertical: 0,
                      horizontal: MediaQuery.of(context).size.width) *
                  0.1,
              child: MessagesPanel(
                agreementId: agreementId,
                partyA: partyA,
                partyB: partyB,
              ),
            ),
          ),
          Expanded(
            flex: 1,
            child: MessageInputPanel(agreementId),
          ),
        ],
      ),
    );
  }
}
