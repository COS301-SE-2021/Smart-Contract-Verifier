//This file will eventually be more professional

import 'package:flutter/material.dart';
import 'package:unison/widgets/contact/contactList_grid.dart';
import 'package:unison/widgets/miscellaneous/funky_text_widget.dart';
import '../widgets/miscellaneous/app_drawer.dart';

class ContactScreen extends StatelessWidget {
  static const routeName = '/my-contacts';
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: FunkyText('Contact Lists'),
      ),
      drawer: AppDrawer(),
      body: ContactListGrid(),
    );
  }
}
