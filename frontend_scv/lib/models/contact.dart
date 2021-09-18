//This class models a contact.
//A contact has a wallet address and a name.
//Be aware of confusion with Contract in terms of naming.

class Contact {

  String address;
  String alias;

  ///Generate a Contact from the relevant JSON
  Contact.fromJSON(Map<String, dynamic> jsn) {

    address = jsn['walletID'];
    alias = jsn['alias'];
  }

}