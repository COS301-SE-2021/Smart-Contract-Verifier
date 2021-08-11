//This class will be a service to be used by the messaging interface.

import 'dart:convert';
import 'package:unison/models/global.dart';
import 'backendAPI.dart';
import '../../models/message.dart';

class MessageService {

  ApiInteraction _api = ApiInteraction();
  final String _reqPath = '/messenger/';

  Future<void> sendMessage(Message mess) async {

    Map<String, String> response;
    try {
      response = await _api.postData(_reqPath + 'send-message', mess.toJSONSend());

      if (response['Status'] != "SUCCESSFUL")
        throw Exception('Message could not be sent');
      } catch (err) {
          print (err); //Handle exception
          throw err;
        }
  }

  Future<List<Message>> getMessages(String id) async { //Get all messages for the agreement id passed in
    return await _getMessageHandler(id, true);
  }

  //Get messages either by agreement, or user id
  Future<List<Message>> _getMessageHandler(String id, bool byAgreement) async { //Used by two other methods

      Map<String, dynamic> body = byAgreement? {'AgreementID' : id, 'RequestingUser' : Global.userAddress} : {'RequestingUser' : id};
      Map<String, String> response;

      try {
        String path = byAgreement? 'agreement' : 'user';
        response = await _api.postData(_reqPath + 'get-all-messages-by-$path', body);

        if (response['Status'] != "SUCCESSFUL")
          throw Exception('Messages could not be retrieved');
      } catch (err) {
        print (err); //Handle exception
        throw err;
      }

      List<Message> ret = [];
      for (int i =0;i<response['Messages'].length;i++) {
        ret.add(Message.fromJSON(jsonDecode(response['Messages'][i])));
      }

      return ret;
  }


  Future<void> setMessageRead(Message mes) async { //Let the backend know that a message has been read

    Map<String, dynamic> body = {'MessageID' : mes.messageID, 'RecipientID' : Global.userAddress};
    Map<String, String> response;

    try {
      response = await _api.postData(_reqPath + 'set-message-as-read', body);

      //RFC: Is error checking even necessary for an 'unimportant' operation?
      if (response['Status'] != "SUCCESSFUL")
        throw Exception('Messages could not be set as read');
    } catch (err) {
      print (err); //Handle exception
      throw err;
    }

  }

  //The following methods are implemented for the sake of completeness, should they ever be needed.

  Future<List<Message>> getAllMessages() async { //Get all messages for the current user, across all agreements.
      return await _getMessageHandler(Global.userAddress, false);
  }


}