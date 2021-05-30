import 'package:flutter/material.dart';

class SupportPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) => Scaffold(
    appBar: AppBar(
      title: Text('Support Page'),
      centerTitle: true,
      backgroundColor: Colors.teal,
    ),
    body: Text(
      'Support goes here',
      style: TextStyle(
        color: Colors.cyanAccent,
      ),
    ),
  );
}
