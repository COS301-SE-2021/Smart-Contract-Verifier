//The user can separate contacts into dedicated lists,
//this class thus holds a list of contacts.

import 'package:unison/models/contact.dart';

class ContactList {

  String name;
  String id; //Server id of the list
  List<Contact> contacts = [];

  ///Generate a contact list from an api result.
  ContactList. fromJSON(Map<String, dynamic> jsn) {

    //var listInfo = jsn['contactListInfo'];
    name = jsn['ContactListName'];
    id = jsn['ContactListID'];
  }

  ///Set the contacts of this contact list
  ///Load them using the relevant ContactService method
  Future<void> setContracts(List<Contact> c) async {
    contacts = List.from(c);
  }


}