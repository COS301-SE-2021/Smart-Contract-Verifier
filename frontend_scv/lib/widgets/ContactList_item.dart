//This is the UI counterpart of ContactList from models.

import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:jdenticon_dart/jdenticon_dart.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:unison/models/contact.dart';
import 'package:unison/models/contactList.dart';
import 'package:unison/screens/view_contact_screen.dart';

import '../models/contract.dart';
import '../providers/auth.dart';
import '../screens/view_contract_screen.dart';

class ContactListItem extends StatelessWidget {
  @override

  ContactList list;

  ContactListItem(ContactList cl) {
    list = cl;
  }

  Widget build(BuildContext context) {
    //final contactList = Provider.of<ContactList>(context);

    return GestureDetector(
      onTap: () {
        print ('A tap');
        Navigator.of(context).pushNamed(
          ViewContactScreen.routeName,
          arguments: [list.id, list.name],
        );
      },
      child: ListTile(
        title: Text(list.name,),
        tileColor: Color.fromRGBO(9, 21, 128, 1.0),//Color.fromRGBO(85, 84, 84, 0.5019607843137255),
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10),),
      ),

    );
  }
}
