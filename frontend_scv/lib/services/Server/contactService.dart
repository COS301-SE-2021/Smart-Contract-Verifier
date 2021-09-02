//This class is used to facilitate the operation of the contact list.

import 'package:unison/models/global.dart';
import 'package:unison/services/Server/apiResponse.dart';
import 'package:unison/services/Server/backendAPI.dart';

class ContactService {

  ApiInteraction _api = ApiInteraction();

  Future<void> createNewList(String name) async {

    await _api.postData('/user/${Global.userAddress}/contactList/$name', {});
  }

  //Add a user to a specific contact list
  Future<void> addUser(String ad, String id) async {

      String path = '/user/${Global.userAddress}/contactList/$id';
      var body = {};
      ApiResponse res = await _api.putData(path, body);
  }

}