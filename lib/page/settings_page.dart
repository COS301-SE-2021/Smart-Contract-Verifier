import 'package:flutter/material.dart';

class SettingsPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) => Scaffold(
    appBar: AppBar(
      title: Text('Settings'),
      centerTitle: true,
      backgroundColor: Colors.teal,
    ),
    body: Text(
      'Settings Page',
      style: TextStyle(
        color: Colors.cyanAccent,
      ),
    ),
  );
}
