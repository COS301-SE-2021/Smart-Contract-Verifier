import 'package:flutter/material.dart';

class ActivityPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) => Scaffold(
    appBar: AppBar(
      title: Text('Activity Stream'),
      centerTitle: true,
      backgroundColor: Colors.teal,
    ),
    body: Text(
      'Activity Page',
      style: TextStyle(
        color: Colors.cyanAccent,
      ),
    ),
  );
}
