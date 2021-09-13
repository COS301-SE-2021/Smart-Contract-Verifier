//This file will eventually be more professional

import 'package:flutter/material.dart';
import 'package:unison/widgets/contact/contactList_grid.dart';
import 'package:unison/widgets/contact/contact_grid.dart';
import 'package:unison/widgets/miscellaneous/funky_text_widget.dart';
import '../widgets/miscellaneous/app_drawer.dart';
import '../widgets/agreement/contracts_grid.dart';

class ContactScreen extends StatelessWidget {
  static const routeName = '/my-contacts';
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: FunkyText('Contact List'),
      ),
      drawer: AppDrawer(),
      body: Center(
        child: ContactListGrid(),
      ),
    );
  }
}
