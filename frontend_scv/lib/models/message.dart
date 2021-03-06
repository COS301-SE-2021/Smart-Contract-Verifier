//This class represents a message sent between two parties.
//This is a preliminary class, and should be expected to change

import 'package:flutter/material.dart';
import 'package:unison/models/global.dart';

import 'global.dart';

class Message with ChangeNotifier {
  String sender;
  String messageText;
  DateTime dateSent;
  String messageID;
  String agreement;

  Message(String text, String agreementId) {
    //Constructor used when a message is newly created;
    sender = Global.userAddress;
    messageText = text;
    agreement = agreementId;
  }

  Message.fromJSON(Map<String, dynamic> jsn) {
    //Generate from backend api response
    this.sender = jsn['sendingUser']['publicWalletID'];
    this.messageText = jsn['message'];
    this.dateSent = DateTime.tryParse(jsn['sendingDate']);
    this.messageID = jsn['messageID'];
  }

  Map<String, dynamic> toJSONSend() {
    //ToJSON when sending a message

    return {
      'Message': messageText,
    };
  }

  Map<String, dynamic> toJSONSetRead() {
    //ToJSON when setting a message as read
    return {
      'MessageID': messageID,
      'RecipientID': Global.userAddress,
    };
  }

  String toString() {
    String ret = 'Sender: ' + sender + '\n';
    ret += 'Text: ' + messageText + '\n';
    ret += 'Date Sent: ' + dateSent.toString();
    return ret;
  }
}
