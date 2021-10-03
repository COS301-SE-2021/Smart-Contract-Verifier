//This widget is an error message on a red background

import 'package:flutter/material.dart';

class errorWidget extends StatelessWidget {
  final String message;

  errorWidget(this.message);

  Widget build(BuildContext context) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        Column(
            mainAxisSize: MainAxisSize.min,
            children: [
          Text(message, style: TextStyle(color: Color.fromRGBO(255, 255, 255, 1.0), fontSize: 20)),
      ],
    )]);
  }

}