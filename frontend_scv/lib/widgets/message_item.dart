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
        return _buildSender(MessageItemType.JurorCurrent);
      } else {
        //Other JUROR or Imposter sent message
        return _buildReceiver(MessageItemType.JurorOther, context);
      }
    } else {
      //Parties:
      if (message.sender == Global.userAddress) {
        //Currently Logged in PARTY sent message
        return _buildSender(MessageItemType.PartyCurrent);
      } else {
        //Other PARTY sent message
        return _buildReceiver(MessageItemType.PartyOther, context);
      }
    }
    return Text('Oops');
  }

  Widget _buildSender(MessageItemType messType) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.end,
      children: [Text(message.messageText)],
    );
  }

  Widget _buildReceiver(MessageItemType messType, BuildContext context) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.start,
      // mainAxisSize: MainAxisSize.min,
      children: [
        // JdenticonSVG(
        //   message.sender,
        //   messType == MessageItemType.PartyOther ? [205] : [100],
        // ),
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
                  messType == MessageItemType.PartyOther ? [205] : [100],
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
                            color: Colors.cyan[400],
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

        // Container(
        //   width: MediaQuery.of(context).size.width * 0.45,
        //   child: ListTile(
        //     title: Text(
        //       message.messageText,
        //       style: TextStyle(fontWeight: FontWeight.normal),
        //     ),
        //     leading:
        //     subtitle: Text(
        //       DateFormat('yyyy-MM-dd kk:mm').format(message.dateSent),
        //       style: TextStyle(
        //         fontSize: 10,
        //       ),
        //     ),
        //     dense: true,
        //     tileColor: Color.fromRGBO(56, 61, 81, 1),
        //   ),
        // ),
        // Text(message.messageText),
      ],
    );
  }
}
