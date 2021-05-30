
import 'package:flutter/material.dart';

class UserProfilePage extends StatelessWidget {
  final String name;
  final String urlImage;

  const UserProfilePage({
    Key? key,
    required this.name,
    required this.urlImage,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) => Scaffold(
    appBar: AppBar(
      backgroundColor: Colors.teal,
      title: Text(name),
      centerTitle: true,
    ),
    body: Text(
      'Profile Page'
    ),
  );
}