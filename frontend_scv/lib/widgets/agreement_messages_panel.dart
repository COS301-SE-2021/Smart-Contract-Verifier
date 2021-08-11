import 'package:flutter/material.dart';
import 'package:unison/models/global.dart';
import 'package:unison/models/message.dart';
import 'package:unison/services/Server/messageService.dart';
import 'package:unison/services/Server/negotiationService.dart';
import 'package:unison/widgets/condition_item.dart';
import '../models/contract.dart';

class AgreementMessagesPanel extends StatelessWidget {
  final List<Message> _messages;

  AgreementMessagesPanel(this._messages);

  @override
  Widget build(BuildContext context) {
    print(_messages);
    return Container(
      // padding: EdgeInsets.all(8),
      child: _messages.isEmpty //handle no messages
          ? Text('No Messages Have Been Sent.') //TODO: Make neater
          : ListView.builder(
              itemCount: _messages.length,
              itemBuilder: (_, index) {
                final message = _messages[index];
                bool isCurrentUser = message.sender == Global.userAddress;
                return Container();
              }),
    );
  }
}
