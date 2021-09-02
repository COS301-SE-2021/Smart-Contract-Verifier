//This class models a contact.
//A contact has a wallet address and a name.
//Be aware of confusion with Contract in terms of naming.

class Contact {

  String address;
  String alias;

  Contact.fromJSON(Map<String, dynamic> jsn) {

    var walAl = jsn['walletAndAlias'];
    address = walAl['walletID'];
    alias = walAl['alias'];
  }

}