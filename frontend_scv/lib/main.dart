import 'package:flutter/material.dart';
import './screens/dashboard_screen.dart';
import './screens/settings_screen.dart';
import './models/contract.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  List<Contract> _contracts = [];

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Smart Contract Verifier',
      theme: ThemeData(
        primarySwatch: Colors.deepOrange,
        accentColor: Colors.cyan,
        canvasColor: Color.fromRGBO(37, 37, 37, 1),
        textTheme: ThemeData.light().textTheme.copyWith(
            body1: TextStyle(
              color: Color.fromRGBO(200, 200, 200, 1),
            ),
            body2: TextStyle(
              color: Color.fromRGBO(105, 105, 105, 1),
            ),
            title: TextStyle(
              fontSize: 20,
              fontWeight: FontWeight.bold,
            )),
      ),
      initialRoute: '/',
      routes: {
        '/': (ctx) => DashboardScreen(_contracts),
        SettingsScreen.routeName: (ctx) => SettingsScreen(),
      },
      onGenerateRoute: (settings) {
        return MaterialPageRoute(
          builder: (ctx) => DashboardScreen(_contracts),
        );
      },
      onUnknownRoute: (settings) {
        return MaterialPageRoute(
          builder: (ctx) => DashboardScreen(_contracts),
        );
      },
    );
  }
}
