import 'package:flutter/material.dart';
import 'package:unison/models/global.dart';
import 'package:unison/models/message.dart';
import 'package:unison/services/Server/messageService.dart';
import 'package:unison/widgets/messaging/message_input_panel.dart';
import 'package:unison/widgets/messaging/messages_panel.dart';
import 'package:unison/widgets/miscellaneous/funky_text_widget.dart';

class EvidenceScreen extends StatefulWidget {
  @override
  _MessagingScreenState createState() => _MessagingScreenState();
  static const routeName = '/evidence-screen';
}

class _MessagingScreenState extends State<EvidenceScreen> {
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
        title: FunkyText('Evidence for Agreement ' + agreementId),
      ),
      // drawer: AppDrawer(),
      body: Center(
        child: Text('Evidence Screen Goes Here'),
      ),
    );
  }
}
