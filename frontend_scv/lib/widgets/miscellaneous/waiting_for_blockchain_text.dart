import 'package:animated_text_kit/animated_text_kit.dart';
import 'package:flutter/material.dart';

class WaitingForBlockChainText extends StatelessWidget {
  // const WaitingForBlockChainText({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    const colorizeColors = [
      Colors.purple,
      Colors.blue,
      Colors.teal,
      Colors.cyan,
      Colors.pink,
    ];

    const colorizeTextStyle = TextStyle(
      fontSize: 20.0,
      // fontFamily: 'Horizon',
    );
    return SizedBox(
      width: 250.0,
      child: AnimatedTextKit(
        animatedTexts: [
          ColorizeAnimatedText(
            'Awaiting Blockchain',
            textAlign: TextAlign.center,
            textStyle: colorizeTextStyle,
            colors: colorizeColors,
          ),
          ColorizeAnimatedText(
            'Waiting for Metamask',
            textAlign: TextAlign.center,
            textStyle: colorizeTextStyle,
            colors: colorizeColors,
          ),
        ],
        isRepeatingAnimation: true,
      ),
    );
  }
}
