import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:unison/models/global.dart';
import 'package:unison/services/Server/messageService.dart';

class MessagesPanel extends StatefulWidget {
  final String agreementId;
  MessagesPanel(this.agreementId);

  @override
  _MessagesPanelState createState() => _MessagesPanelState();
}

class _MessagesPanelState extends State<MessagesPanel> {
  @override
  Widget build(BuildContext context) {
    MessageService messageService = MessageService();
    // print(messages);
    // return Text(messages.toString());
    return FutureBuilder(
        future: messageService.getMessages(widget.agreementId),
        builder: (context, messagesSnapshot) {
          return messagesSnapshot.connectionState != ConnectionState.done
              ? CircularProgressIndicator()
              : //Text(messagesSnapshot.data);

              ListView.builder(
                  padding: const EdgeInsets.all(10.0),
                  itemCount: messagesSnapshot.data.length,
                  itemBuilder: (BuildContext context, int index) {
                    final message = messagesSnapshot.data[index];
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
                                ),
                              SizedBox(
                                width: 10,
                              ),
                              Container(
                                padding: EdgeInsets.all(10),
                                constraints: BoxConstraints(
                                    maxWidth:
                                        MediaQuery.of(context).size.width *
                                            0.6),
                                decoration: BoxDecoration(
                                    color: isCurrentUser
                                        ? Colors.deepOrange
                                        : Colors.cyan,
                                    borderRadius: BorderRadius.only(
                                      topLeft: Radius.circular(16),
                                      topRight: Radius.circular(16),
                                      bottomLeft: Radius.circular(
                                          isCurrentUser ? 12 : 0),
                                      bottomRight: Radius.circular(
                                          isCurrentUser ? 0 : 12),
                                    )),
                                child: Text(
                                  messagesSnapshot.data[index].messageText,
                                  // style: MyTheme.bodyTextMessage.copyWith(
                                  //     color: isCurrentUser ? Colors.white : Colors.grey[800]),
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
                                SizedBox(
                                  width: 8,
                                ),
                                Text(
                                  DateFormat('yyyy-MM-dd hh:mm')
                                      .format(message.dateSent),
                                  style: TextStyle(
                                    fontSize: 10,
                                  ),
                                )
                              ],
                            ),
                          )
                        ],
                      ),
                    );
                  },
                );
        });
  }
}