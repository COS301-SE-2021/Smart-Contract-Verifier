//This class is used to facilitate the operation of the contact list.

import 'package:unison/models/contact.dart';
import 'package:unison/models/contactList.dart';
import 'package:unison/models/global.dart';
import 'package:unison/services/Server/apiResponse.dart';
import 'package:unison/services/Server/backendAPI.dart';

class ContactService {

  ApiInteraction _api = ApiInteraction();

  ///Create a new list of contacts
  Future<void> createNewList(String name) async {

    ApiResponse res = await _api.postData('/user/${Global.userAddress}/contactList/$name', {});
  }

  ///Get all contact lists for a user
  Future<List<ContactList>> getContactLists() async {

    ApiResponse res = await _api.getData('/user/${Global.userAddress}/contactList/');
    if (!res.successful)
      throw 'Could not retrieve contact list. Details:\n' + res.errorMessage;

    List<ContactList> ret = [];
    for (var i in res.result['contactListInfo']) {
        ret.add(ContactList.fromJSON(i));
    }

    return ret;

  }
  
  ///Get a list of contacts for a ContactList by id.
  ///Should be used in conjunction with ContactList.setContacts()
  Future<List<Contact>> getContacts(String id) async {
    ApiResponse res = await _api.getData('/user/${Global.userAddress}/contactList/$id');
    if (!res.successful)
      throw 'Could not retrieve contacts. Details:\n' + res.errorMessage;

    List<Contact> ret = [];
    var list = res.result['walletAndAlias'];
    for (var i in list) {
      ret.add(Contact.fromJSON(i));
    }

    return ret;
  }

  ///Add a user to a specific contact list
  Future<void> addUser(String ad, String name, String id) async {

      String path = '/user/${Global.userAddress}/contactList/$id';
      var body = {'NewUserID' : ad, 'NewUserAlias' : name};
      ApiResponse res = await _api.putData(path, body);
      if (!res.successful)
        throw 'Could not add user to contact list. Details:\n' + res.errorMessage;
  }

  ///Remove a user from a contact list
  Future<void> removeUser(String ad, String id) async {

      String path = '/user/${Global.userAddress}/contactList/$id/$ad';
      ApiResponse res = await _api.deleteData(path);
      if (!res.successful)
        throw 'Could not remove user from contact list. Details:\n' + res.errorMessage;
  }

}