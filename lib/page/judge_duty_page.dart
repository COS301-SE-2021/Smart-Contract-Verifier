import 'package:flutter/material.dart';

class JudgeDutyPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) => Scaffold(
    appBar: AppBar(
      title: Text('Judge Duty'),
      centerTitle: true,
      backgroundColor: Colors.teal,
    ),
    body: Text(
      'Judge Page',
      style: TextStyle(
        color: Colors.cyanAccent,
      ),
    ),
  );
}
