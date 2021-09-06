//This file will eventually be more professional

import 'package:flutter/material.dart';
import 'package:unison/widgets/contactList_grid.dart';
import 'package:unison/widgets/contact_grid.dart';
import '../widgets/app_drawer.dart';
import '../widgets/contracts_grid.dart';

class ContactScreen extends StatelessWidget {
  static const routeName = '/my-contacts';
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Contacts'),
      ),
      drawer: AppDrawer(),
      body: Center(
        child: ContactListGrid(),
      ),
    );
  }

}