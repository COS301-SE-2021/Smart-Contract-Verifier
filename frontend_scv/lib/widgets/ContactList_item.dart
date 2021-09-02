//This is the UI counterpart of ContactList from models.

import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:jdenticon_dart/jdenticon_dart.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:unison/models/contact.dart';
import 'package:unison/models/contactList.dart';

import '../models/contract.dart';
import '../providers/auth.dart';
import '../screens/view_contract_screen.dart';

class ContactListItem extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    final contactList = Provider.of<ContactList>(context);

    return CircularProgressIndicator();
    // return GestureDetector(
    //   onTap: () {
    //     Navigator.of(context).pushNamed(
    //       //ViewContactScreen.routeName,
    //       arguments: contactList.id,
    //     );
    //   },
    //   child: ListTile(
    //     title: Text(contactList.name),
    //
    //   ),
    // );
  }
}
