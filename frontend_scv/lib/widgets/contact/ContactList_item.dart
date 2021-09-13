//This is the UI counterpart of ContactList from models.

import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:jdenticon_dart/jdenticon_dart.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:unison/effects/glow_on_hover.dart';
import 'package:unison/models/contact.dart';
import 'package:unison/models/contactList.dart';
import 'package:unison/screens/view_contact_screen.dart';

import '../../models/contract.dart';
import '../../providers/auth.dart';
import '../../screens/view_contract_screen.dart';

class ContactListItem extends StatelessWidget {
  final ContactList list;
  ContactListItem(this.list);

  @override
  Widget build(BuildContext context) {
    Widget contactListItem = GestureDetector(
      onTap: () {
        print('A tap');
        Navigator.of(context).pushNamed(
          ViewContactScreen.routeName,
          arguments: [list.id, list.name],
        );
      },
      child: Card(
        color: Color.fromRGBO(56, 61, 81, 1),
        elevation: 15,
        child: ListTile(
          title: Text(
            list.name,
          ),
        ),
      ),
    );

    return GlowOnHover(contactListItem);
  }
}
