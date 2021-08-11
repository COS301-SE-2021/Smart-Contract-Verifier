//This class represents a message sent between two parties.
//This is a preliminary class, and should be expected to change

import 'global.dart';

class Message {
  String sender;
  String messageText;
  DateTime dateSent;
  String messageID;
  String agreement;

  //TODO: add time to allow for ordering of messages
  Message(String text) {
    //Constructor used when a message is newly created;
    sender = Global.userAddress;
    messageText = text;
  }

  // Message.fromJSON(Map<String, dynamic> jsn) {
  //   this.sender = jsn['sender'];
  //   this.messageText = jsn['messageText'];
  //   //timestamp/send time?
  // }

  //Below is the final implementation
  Message.fromJSON(Map<String, dynamic> jsn) { //Generate from backend api response
    this.sender = jsn['sendingUser']['publicWalletID'];
    print (this.sender);
    this.messageText = jsn['message'];
    this.dateSent = DateTime.tryParse(jsn['sendingDate']);
    this.messageID = jsn['messageID'];

  }

  Map<String, dynamic> toJSONSend() { //ToJSON when sending a message
    return {
      'SendingUser' : Global.userAddress,
      'AgreementID' : agreement,
      'Message' : messageText,
    };
  }

  String toString() {

    String ret = 'Sender: '+ sender + '\n';
    ret += 'Text: ' + messageText + '\n';
    ret += 'Date Sent: ' + dateSent.toString();
    return ret;
  }


}
