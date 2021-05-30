import 'package:flutter/material.dart';

class ActionRequiredPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) => Scaffold(
        appBar: AppBar(
          title: Text('Required Action'),
          centerTitle: true,
          backgroundColor: Colors.teal,
        ),
        body: Text(
          'Action Page',
          style: TextStyle(
            color: Colors.cyanAccent,
          ),
        ),
      );
}
