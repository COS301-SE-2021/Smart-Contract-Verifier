import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:unison/models/global.dart';
import 'package:unison/screens/contacts_screen.dart';
import 'package:unison/screens/judge_duty_screen.dart';

import 'package:flutter/services.dart';
import 'package:unison/screens/statsScreen.dart';
import 'package:unison/widgets/miscellaneous/funky_text_widget.dart';
import 'package:url_launcher/url_launcher.dart';

import '../../providers/auth.dart';

class AppDrawer extends StatelessWidget {
  final snackBar = SnackBar(
    backgroundColor: Color.fromRGBO(56, 61, 81, 1),
    content: Row(mainAxisAlignment: MainAxisAlignment.center, children: [
      Text(
        'Address Copied to Clipboard',
        style: TextStyle(color: Colors.pink, fontSize: 16),
      )
    ]),
  );
  @override
  Widget build(BuildContext context) {
    return Drawer(
      child: Column(
        children: [
          AppBar(
            title: FunkyText('Unison.'),
            automaticallyImplyLeading: false, //never adds a back button
          ),
          Divider(),
          ListTile(
            // leading: Icon(Icons.list_alt),
            title: Text(Global.userAddress),
            trailing: IconButton(
              color: Colors.blue,
              onPressed: () async {
                ClipboardData data =
                    ClipboardData(text: '<Text to copy goes here>');
                await Clipboard.setData(data);

                Clipboard.setData(
                  ClipboardData(text: Global.userAddress.toString()),
                );
                ScaffoldMessenger.of(context).showSnackBar(snackBar);
              },
              icon: Icon(
                Icons.copy,
                color: Colors.grey,
              ),
            ),
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
            leading: Icon(Icons.contacts),
            title: Text('My Contacts'),
            onTap: () {
              Navigator.of(context)
                  .pushReplacementNamed(ContactScreen.routeName);
            },
          ),
          Divider(),
          ListTile(
            leading: Icon(Icons.gavel),
            title: Text('Judge Duty'),
            onTap: () {
              Navigator.of(context)
                  .pushReplacementNamed(JudgeDutyScreen.routeName);
            },
          ),
          Divider(),
          ListTile(
            leading: Icon(Icons.auto_graph_sharp),
            title: Text('Some Statistics'),
            onTap: () {
              Navigator.of(context).pushReplacementNamed(StatsScreen.routeName);
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
          Divider(),
          Divider(),
          TextButton(onPressed: () async {await launch('https://forms.gle/KaZP94e2iF2zodPF7');}, child: Text('Feedback')),
          SizedBox(height: 5),
          TextButton(onPressed: () async {await launch('https://drive.google.com/file/d/1mfi73z7QxIC34tXWid9Nj7GetGbABWW6/view');}, child: Text('User manual')),
        ],
      ),
    );
  }
}
