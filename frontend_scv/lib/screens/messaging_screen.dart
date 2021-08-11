import 'package:flutter/material.dart';
import 'package:unison/widgets/message_input_panel.dart';
import 'package:unison/widgets/messages_panel.dart';

class MessagingScreen extends StatefulWidget {
  @override
  _MessagingScreenState createState() => _MessagingScreenState();
  static const routeName = '/messaging-screen';
}

class _MessagingScreenState extends State<MessagingScreen> {
  var _isLoading = false;

  @override
  Widget build(BuildContext context) {
    final agreementId = ModalRoute.of(context).settings.arguments as String;

    print('Agreement ID [Messaging Screen]: ' + agreementId);

    return Scaffold(
      appBar: AppBar(
        // title: Text(Global.userAddress),
        title: Text('Chat for agreement: ' + agreementId),
      ),
      // drawer: AppDrawer(),
      body: _isLoading
          ? Center(
              child: CircularProgressIndicator(),
            )
          : Column(
              children: [
                Expanded(
                  flex: 6,
                  child: MessagesPanel(agreementId),
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
