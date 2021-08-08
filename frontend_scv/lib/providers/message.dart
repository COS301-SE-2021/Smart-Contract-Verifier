//This class represents a message sent between two parties.
//This is a preliminary class, and should be expected to change

import 'global.dart';

class Message {

  String sender;
  String messageText;

  Message(String text) { //Constructor used when a message is newly created;
    sender = Global.userAddress;
    messageText = text;
  }

  Message.fromJSON(Map<String, dynamic> jsn) {
    this.sender = '';
    this.messageText = '';
  }

}