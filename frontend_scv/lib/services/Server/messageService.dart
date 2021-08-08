//This class will be a service to be used by the messaging service.

import 'backendAPI.dart';
import '../../providers/message.dart';

class MessageService {

  ApiInteraction api = ApiInteraction();

  Future<void> sendMessage(Message mess) async {
    //TODO list:
    //Get api endpoints
    //Pass in relevant things
    //Error handling

  }

  Future<List<Message>> getMessages(String id) async { //Will usually be the current user, nut this implementation for consistency
    //TODO list:
    //Get endpoints
    //Parse result
    //Build list of messages
    //Error handling

    return [];
  }


}