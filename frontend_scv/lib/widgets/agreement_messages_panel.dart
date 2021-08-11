import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
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
                return Container(
                  margin: EdgeInsets.only(top: 10),
                  child: Column(
                    children: [
                      Row(
                        mainAxisAlignment: isCurrentUser
                            ? MainAxisAlignment.end
                            : MainAxisAlignment.start,
                        crossAxisAlignment: CrossAxisAlignment.end,
                        children: [
                          if (!isCurrentUser)
                            CircleAvatar(
                              radius: 15,
                              // backgroundImage: AssetImage(user.avatar),
                              //TODO: use Jdicons here
                            ),
                          SizedBox(
                            width: 10,
                          ),
                          Container(
                            padding: EdgeInsets.all(10),
                            constraints: BoxConstraints(
                                maxWidth:
                                    MediaQuery.of(context).size.width * 0.6),
                            decoration: BoxDecoration(
                                color: isCurrentUser
                                    ? Colors.deepOrange
                                    : Colors.grey[200],
                                borderRadius: BorderRadius.only(
                                  topLeft: Radius.circular(16),
                                  topRight: Radius.circular(16),
                                  bottomLeft:
                                      Radius.circular(isCurrentUser ? 12 : 0),
                                  bottomRight:
                                      Radius.circular(isCurrentUser ? 0 : 12),
                                )),
                            child: Text(
                              _messages[index].messageText,
                              // TODO: style: MyTheme.bodyTextMessage.copyWith(
                              //     color: isMe ? Colors.white : Colors.grey[800]),
                            ),
                          ),
                        ],
                      ),
                      Padding(
                        padding: const EdgeInsets.only(top: 5),
                        child: Row(
                          mainAxisAlignment: isCurrentUser
                              ? MainAxisAlignment.end
                              : MainAxisAlignment.start,
                          children: [
                            if (!isCurrentUser)
                              SizedBox(
                                width: 40,
                              ),
                            Icon(
                              Icons.done_all,
                              size: 20,
                              // color: MyTheme.bodyTextTime.color,
                            ),
                            SizedBox(
                              width: 8,
                            ),
                            Text(
                              DateFormat('yyyy-MM-dd – kk:mm')
                                  .format(message.dateSent),
                              // style: MyTheme.bodyTextTime,
                            )
                          ],
                        ),
                      )
                    ],
                  ),
                );
              }),
    );
  }
}
