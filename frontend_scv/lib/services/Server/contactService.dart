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

    print ('Before call');
    ApiResponse res = await _api.getData('/user/${Global.userAddress}/contactList/');
    print ('After call');
    List<ContactList> ret = [];
    print (res.result['contactListInfo']);
    for (var i in res.result['contactListInfo']) {
      try {
        ret.add(ContactList.fromJSON(i));
        print ('Added');
      } catch (e) {
        print ('Error: ' + e);
      }
    }

    print ('Returning: ' + ret.toString());
    return ret;

  }
  
  ///Get a list of contacts for a ContactList by id.
  ///Should be used in conjunction with ContactList.setContacts()
  Future<List<Contact>> getContacts(String id) async {
    ApiResponse res = await _api.getData('/user/${Global.userAddress}/contactList/$id');
    List<Contact> ret = [];
    var list = res.result['walletAndAlias'];
    for (var i in list) {
      ret.add(Contact.fromJSON(i));
    }

    return ret;
  }

  ///Add a user to a specific contact list
  Future<void> addUser(String ad, String id) async {

      String path = '/user/${Global.userAddress}/contactList/$id';
      var body = {};
      ApiResponse res = await _api.putData(path, body);

  }

  ///Remove a user from a contact list
  Future<void> removeUser(String ad, String id) async {

      String path = '/user/${Global.userAddress}/contactList/$id/$ad';
      ApiResponse res = await _api.deleteData(path);
  }

}