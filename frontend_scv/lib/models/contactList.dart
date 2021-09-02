//The user can separate contacts into dedicated lists,
//this class thus holds a list of contacts.

class ContactList {

  String name;
  String id; //Server id of the list

  ContactList.fromJSON(Map<String, dynamic> jsn) {

    var listInfo = jsn['contactListInfo'];
    name = listInfo['ContactListName'];
    id = listInfo['ContactListID'];
  }


}