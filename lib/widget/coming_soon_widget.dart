import 'package:flutter/material.dart';

class ComingSoon extends StatelessWidget {


  @override
  Widget build(BuildContext context) {

    return Container(
      margin: EdgeInsets.all(100),
      // padding: EdgeInsets.all(20),
      child: Column(
        children: [

          Center(
              child: Text(
            "Coming Soon",
            style: TextStyle(
              color: Colors.teal,
              fontSize: 30,
            ),
          )),
          Center(
            child: Image.asset(
              'graphics/cena.gif',
              colorBlendMode: BlendMode.darken,
              height: 88,
              alignment: Alignment.center,
            ),
          )
        ],
      ),
    );
  }
}
