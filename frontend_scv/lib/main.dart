import 'package:flutter/material.dart';
import 'package:frontend_scv/screens/contract_detail_screen.dart';
import './screens/dashboard_screen.dart';
import './screens/settings_screen.dart';
import './models/contract.dart';
import './providers/contracts.dart';
import 'package:provider/provider.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    // List<Contract> _contracts = [agreementB];
    return ChangeNotifierProvider(
      create: (ctx) => Contracts(),
      child: MaterialApp(
        debugShowCheckedModeBanner: false,
        title: 'Unison',
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
          SettingsScreen.routeName: (ctx) => SettingsScreen(),
          ContractDetailScreen.routeName: (ctx) => ContractDetailScreen(),
        },
        home: DashboardScreen(),
      ),
    );
  }
}
