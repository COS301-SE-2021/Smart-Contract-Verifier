import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:unison/models/global.dart';
import 'package:unison/widgets/MessagesPanel.dart';

import '../screens/edit_contract_screen.dart';
import '../widgets/app_drawer.dart';

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
          : MessagesPanel(agreementId),
    );
  }
}
