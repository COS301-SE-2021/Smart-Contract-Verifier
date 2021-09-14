//Not to be confused with contractgrid

import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:unison/models/contact.dart';
import 'package:unison/models/contactList.dart';

import '../agreement/contract_item.dart';
import '../../models/contracts.dart';
import 'contact_item.dart';

class ContactGrid extends StatelessWidget {
  ContactGrid();
  @override
  Widget build(BuildContext context) {
    //-> setting/getting contracts happens in the contracts_overview_screen
    final contacts = Provider.of<ContactList>(context); //simply access
    var _firstUser = false;
    if (contacts == null) {
      _firstUser = true;
    }

    return CircularProgressIndicator();

    // return ListView.builder(
    //   padding: const EdgeInsets.all(10.0),
    //   itemCount: _firstUser ? 0 : contacts.length,
    //   itemBuilder: (ctx, i) => ChangeNotifierProvider.value(
    //     value: contacts[i],
    //     child: ContactItem(),
    //   ),
    // );
  }
}
