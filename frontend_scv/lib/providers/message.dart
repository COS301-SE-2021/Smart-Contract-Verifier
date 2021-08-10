//This class represents a message sent between two parties.
//This is a preliminary class, and should be expected to change

import 'global.dart';

class Message {
  String sender;
  String messageText;
  //TODO: add time to allow for ordering of messages
  Message(String text) {
    //Constructor used when a message is newly created;
    sender = Global.userAddress;
    messageText = text;
  }

  Message.fromJSON(Map<String, dynamic> jsn) {
    this.sender = jsn['sender'];
    this.messageText = jsn['messageText'];
    //timestamp/send time?
  }
}
