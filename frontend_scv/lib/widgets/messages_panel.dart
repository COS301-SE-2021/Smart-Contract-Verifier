import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:intl/intl.dart';
import 'package:unison/models/global.dart';
import 'package:unison/models/message.dart';
import 'package:unison/services/Server/messageService.dart';
import 'package:unison/widgets/jdenticon_svg.dart';

class MessagesPanel extends StatefulWidget {
  final String agreementId;
  MessagesPanel(this.agreementId);
  List<Message> agreementMessages = [];
  @override
  _MessagesPanelState createState() => _MessagesPanelState();
}

class _MessagesPanelState extends State<MessagesPanel> {
  MessageService messageService = MessageService();

  @override
  Widget build(BuildContext context) {
    return StreamBuilder(
      stream: messageService.getNewMessageStream(widget.agreementId),
      builder: (ctx, streamSnapshot) {
        if (streamSnapshot.connectionState == ConnectionState.waiting) {
          return Center(
            child: CircularProgressIndicator(),
          );
        }
        final messages = streamSnapshot.data;
        return ListView.builder(
          itemCount: messages.length,
          itemBuilder: (ctx, index) => Container(
            padding: EdgeInsets.all(8),
            child: Text(messages[index].messageText),
          ),
        );
      },
    );
  }
/*
  Check for new messages on server
  - if new message
    -> push all message through stream
 */

}
