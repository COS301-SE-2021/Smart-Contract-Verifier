//This class will be a service to be used by the messaging service.

import 'dart:convert';

import 'package:unison/providers/global.dart';

import 'backendAPI.dart';
import '../../providers/message.dart';

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

    Map<String, dynamic> body = {'AgreementID' : id, 'RequestingUser' : Global.userAddress};
    Map<String, String> response;

    try {
      response = await _api.postData(_reqPath + 'get-all-messages-by-agreement', body);

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


}