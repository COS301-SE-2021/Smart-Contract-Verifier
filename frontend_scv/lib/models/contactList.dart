//The user can separate contacts into dedicated lists,
//this class thus holds a list of contacts.

import 'package:unison/models/contact.dart';

class ContactList {

  String name;
  String id; //Server id of the list
  List<Contact> contacts = [];

  ContactList.fromJSON(Map<String, dynamic> jsn) {

    var listInfo = jsn['contactListInfo'];
    name = listInfo['ContactListName'];
    id = listInfo['ContactListID'];
  }



}