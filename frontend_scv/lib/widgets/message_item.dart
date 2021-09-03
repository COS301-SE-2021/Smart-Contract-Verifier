import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:unison/models/global.dart';
import 'package:unison/models/message.dart';
import 'package:unison/widgets/jdenticon_svg.dart';

enum MessageItemType { JurorCurrent, JurorOther, PartyCurrent, PartyOther }

class MessageItem extends StatelessWidget {
  final String partyA;
  final String partyB;
  final Message message;
  MessageItem(this.message, this.partyA, this.partyB);

  @override
  Widget build(BuildContext context) {
    if (message.sender != partyA && message.sender != partyB) {
      //Judges:
      if (message.sender == Global.userAddress) {
        //Currently Logged in JUROR sent message
        return _buildSender(MessageItemType.JurorCurrent, context);
      } else {
        //Other JUROR or Imposter sent message
        return _buildReceiver(MessageItemType.JurorOther, context);
      }
    } else {
      //Parties:
      if (message.sender == Global.userAddress) {
        //Currently Logged in PARTY sent message
        return _buildSender(MessageItemType.PartyCurrent, context);
      } else {
        //Other PARTY sent message
        return _buildReceiver(MessageItemType.PartyOther, context);
      }
    }
    return Text('Oops');
  }

  Widget _buildReceiver(MessageItemType messType, BuildContext context) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.start,
      children: [
        Card(
          elevation: 5.0,
          shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.only(
            topLeft: Radius.circular(20.0),
            bottomLeft: Radius.circular(0.0),
            topRight: Radius.circular(20.0),
            bottomRight: Radius.circular(20.0),
          )),
          margin: EdgeInsets.all(5.0),
          color: Color.fromRGBO(56, 61, 81, 1),
          child: Padding(
            padding: EdgeInsets.fromLTRB(12.0, 8.0, 12.0, 8.0),
            child: Row(
              children: [
                JdenticonSVG(
                  message.sender,
                  messType == MessageItemType.PartyOther ? [205] : [150],
                ),
                SizedBox(
                  width: 5,
                ),
                Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Text(
                          message.sender.substring(0, 6) +
                              '...' +
                              message.sender.substring(
                                  message.sender.length - 4,
                                  message.sender.length),
                          style: TextStyle(
                            color: messType == MessageItemType.PartyOther
                                ? Colors.cyan[400]
                                : Colors.teal[400],
                            fontSize: 10,
                          ),
                        ),
                        SizedBox(
                          width: 5,
                        ),
                      ],
                    ),
                    Text(
                      message.messageText,
                      style: TextStyle(
                        // color: Colors.white,
                        fontSize: 16,
                      ),
                    ),
                    Text(
                      DateFormat('yyyy-MM-dd kk:mm').format(message.dateSent),
                      style: TextStyle(fontSize: 10, color: Colors.white54),
                    ),
                  ],
                ),
              ],
            ),
          ),
        ),
      ],
    );
  }

  Widget _buildSender(MessageItemType messType, BuildContext context) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.end,
      children: [
        Card(
          elevation: 5.0,
          shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.only(
            topLeft: Radius.circular(20.0),
            bottomLeft: Radius.circular(20.0),
            topRight: Radius.circular(20.0),
            bottomRight: Radius.circular(0.0),
          )),
          margin: EdgeInsets.all(5.0),
          color: Color.fromRGBO(43, 45, 60, 1),
          child: Padding(
            padding: EdgeInsets.fromLTRB(12.0, 8.0, 12.0, 8.0),
            child: Row(
              children: [
                Column(
                  crossAxisAlignment: CrossAxisAlignment.end,
                  children: [
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Text(
                          message.sender.substring(0, 6) +
                              '...' +
                              message.sender.substring(
                                  message.sender.length - 4,
                                  message.sender.length),
                          style: TextStyle(
                            color: messType == MessageItemType.PartyCurrent
                                ? Colors.pink[400]
                                : Colors.teal[400],
                            fontSize: 10,
                          ),
                        ),
                        SizedBox(
                          width: 5,
                        ),
                      ],
                    ),
                    Text(
                      message.messageText,
                      style: TextStyle(
                        // color: Colors.white,
                        fontSize: 16,
                      ),
                    ),
                    Text(
                      DateFormat('yyyy-MM-dd kk:mm').format(message.dateSent),
                      style: TextStyle(fontSize: 10, color: Colors.white54),
                    ),
                  ],
                ),
                // JdenticonSVG(
                //   message.sender,
                //   messType == MessageItemType.PartyCurrent ? [340] : [150],
                // ),
                SizedBox(
                  width: 15,
                ),
              ],
            ),
          ),
        ),
      ],
    );
  }
}
