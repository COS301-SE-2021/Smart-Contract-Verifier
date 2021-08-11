import 'package:flutter/material.dart';
import 'package:unison/models/message.dart';
import 'package:unison/services/Server/messageService.dart';
import 'package:unison/services/Server/negotiationService.dart';
import 'package:unison/widgets/condition_item.dart';
import '../models/contract.dart';
import 'message_item.dart';

class AgreementMessagesPanel extends StatelessWidget {
  final List<Message> _messages;

  AgreementMessagesPanel(this._messages);

  @override
  Widget build(BuildContext context) {
    MessageService mesS = MessageService();

    print(_messages);
    return Container(
      // padding: EdgeInsets.all(8),
      child: _messages.isEmpty //handle no messages
          ? Text('No Messages Have Been Sent.')
          : ListView.builder(
              itemCount: _messages.length,
              itemBuilder: (_, i) => Column(
                children: [
                  Text('Sender: ${_messages[i].sender}'
                      '\nMessage:${_messages[i].messageText}'),
                  MessageItem(
                    message: _messages[i],
                    messageService: mesS,
                  ),
                  Divider(),
                ],
              ),
            ),
    );
  }
}
