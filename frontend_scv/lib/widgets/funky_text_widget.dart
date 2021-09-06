import 'package:flutter/material.dart';

class FunkyText extends StatelessWidget {
  final String _text;

  FunkyText(this._text);

  @override
  Widget build(BuildContext context) {
    return ShaderMask(
      shaderCallback: (bounds) => LinearGradient(colors: [
        Color.fromRGBO(50, 183, 196, 1),
        Color.fromRGBO(167, 89, 160, 1)
      ]).createShader(
        Rect.fromLTWH(0, 0, bounds.width, bounds.height),
      ),
      child: Text(
        _text,
        style: TextStyle(
          // The color must be set to white for this to work
          color: Colors.white,
          // fontSize: 50,
        ),
      ),
    );
  }
}
