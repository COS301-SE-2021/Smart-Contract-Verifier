import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../providers/auth.dart';

class AppDrawer extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Drawer(
      child: Column(
        children: [
          AppBar(
            title: Text('Unison.'),
            automaticallyImplyLeading: false, //never adds a back button
          ),
          Divider(),
          ListTile(
            leading: Icon(Icons.list_alt),
            title: Text('Dashboard'),
            onTap: () {
              Navigator.of(context).pushReplacementNamed('/');
            },
          ),
          Divider(),
          ListTile(
            leading: Icon(Icons.gavel),
            title: Text('Judge Duty'),
            onTap: () {
              //TODO
              // Navigator.of(context)
              //     .pushReplacementNamed(JudgeDutyScreen.routeName);
            },
          ),
          Divider(),
          ListTile(
            leading: Icon(Icons.logout),
            title: Text('Logout'),
            onTap: () {
              Navigator.of(context).pop(); //Close the drawer (use if an
              // error occurs)
              Navigator.of(context).pushReplacementNamed('/');
              Provider.of<Auth>(context, listen: false).logout();
            },
          ),
        ],
      ),
    );
  }
}
