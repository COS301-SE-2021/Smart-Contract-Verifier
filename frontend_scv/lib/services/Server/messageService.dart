//This class will be a service to be used by the messaging interface.

import 'package:unison/models/global.dart';

import '../../models/message.dart';
import 'backendAPI.dart';

class MessageService {
  ApiInteraction _api = ApiInteraction();
  final String _reqPath = '/user/' + Global.userAddress;

  Future<void> sendMessage(Message mess) async {
    var response;
    try {
      response =
          await _api.postData('$_reqPath/agreement/${mess.messageID }/message', mess.toJSONSend());

      if (response['Status'] != "SUCCESSFUL")
        throw Exception('Message could not be sent');
    } catch (err) {
      print(err); //Handle exception
      throw err;
    }
  }

  Future<List<Message>> getMessages(String id) async {
    //Get all messages for the agreement id passed in
    return await _getMessageHandler(id, true);
  }

  //Get messages either by agreement, or user id
  Future<List<Message>> _getMessageHandler(String id, bool byAgreement) async {
    //Used by two other methods

    Map<String, dynamic> body = byAgreement
        ? {'AgreementID': id, 'RequestingUser': Global.userAddress}
        : {'RequestingUser': id};
    var response;

    String path = '$_reqPath/' + (byAgreement? 'agreement/$id/message': 'message');

    try {
      response = await _api.getData(path);

      if (response['Status'] != "SUCCESSFUL")
        throw Exception('Messages could not be retrieved');
    } catch (err) {
      print(err); //Handle exception
      throw err;
    }

    List<Message> ret = [];
    for (int i = 0; i < response['Messages'].length; i++) {
      ret.add(Message.fromJSON((response['Messages'][i])));
    }

    return ret;
  }

  Future<void> setMessageRead(Message mes) async {
    //Let the backend know that a message has been read

    var response;

    try {
      response = await _api.postData(
          _reqPath + 'set-message-as-read', mes.toJSONSetRead());

      //RFC: Is error checking even necessary for an 'unimportant' operation?
      if (response['Status'] != "SUCCESSFUL")
        throw Exception('Messages could not be set as read');
    } catch (err) {
      print('Is this here? ' + err); //Handle exception
      throw err;
    }
  }

  //The following methods are implemented for the sake of completeness, should they ever be needed.

  Future<List<Message>> getAllMessages() async {
    //Get all messages for the current user, across all agreements.
    return await _getMessageHandler(Global.userAddress, false);
  }

  //This method returns a stream which checks for unread messages every 3 seconds, and yields any unread messages.
  Stream<Message> getNewMessageStream(String id) async* {
    Duration interval = Duration(seconds: 3);
    //Check every 3 seconds

    //This will change soon.
    Map<String, String> body = {};

    while (true) {
      await Future.delayed(interval);
      final response = await _api.postData(_reqPath + 'getUnread', body);

      if (response['Status'] != "SUCCESSFUL")
        throw Exception('Messages could not be retrieved');

      for (int i = 0; i < response['Messages'].length; i++) { //Yield any unread messages
        yield Message.fromJSON((response['Messages'][i]));
      }
    }

  }

}
