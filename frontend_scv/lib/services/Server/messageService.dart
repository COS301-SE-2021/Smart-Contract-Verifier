//This class will be a service to be used by the messaging interface.

import 'package:unison/models/global.dart';

import '../../models/message.dart';
import 'apiResponse.dart';
import 'backendAPI.dart';

class MessageService {
  ApiInteraction _api = ApiInteraction();
  final String _reqPath = '/user/' + Global.userAddress;

  Future<void> sendMessage(Message mess) async {
    ApiResponse response =
          await _api.postData('$_reqPath/agreement/${mess.agreement }/message', mess.toJSONSend());

      if (!response.successful)
        throw Exception('Message could not be sent');

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


    String path = '$_reqPath/' + (byAgreement? 'agreement/$id/message': 'message');
    ApiResponse response = await _api.getData(path);

    if (!response.successful)
      throw Exception('Messages could not be retrieved');

    List<Message> ret = [];
    for (int i = 0; i < response.result.length; i++) {
      ret.add(Message.fromJSON((response.result['Messages'][i])));
    }

    return ret;
  }

  Future<void> setMessageRead(Message mes) async {
    //Let the backend know that a message has been read
    ApiResponse res = await _api.putData('$_reqPath/message/${mes.messageID}');
    //Can check if successful here

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

      if (!response.successful)
        throw Exception('Messages could not be retrieved');

      for (int i = 0; i < response.result.length; i++) { //Yield any unread messages
        yield Message.fromJSON((response.result['Messages'][i]));
      }
    }

  }

}
