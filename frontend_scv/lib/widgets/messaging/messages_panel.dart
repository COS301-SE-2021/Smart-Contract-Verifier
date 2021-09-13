import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:unison/models/message.dart';
import 'package:unison/services/Server/messageService.dart';

import 'message_item.dart';

class MessagesPanel extends StatefulWidget {
  final String agreementId;
  final String partyA;
  final String partyB;
  MessagesPanel({this.agreementId, this.partyA, this.partyB});

  final List<Message> agreementMessages = [];
  @override
  _MessagesPanelState createState() => _MessagesPanelState();
}

class _MessagesPanelState extends State<MessagesPanel> {
  MessageService messageService = MessageService();
  ScrollController listScrollController = ScrollController();
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
            child: MessageItem(messages[index], widget.partyA, widget.partyB),
          ),
        );
      },
    );
  }
}
