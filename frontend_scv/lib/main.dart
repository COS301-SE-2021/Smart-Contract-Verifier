import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:unison/screens/judge_duty_screen.dart';

import './screens/contracts_overview_screen.dart';
import './screens/auth_screen.dart';
import './screens/splash_screen.dart';
import './screens/edit_contract_screen.dart';
import './screens/view_contract_screen.dart';
//
import './providers/contracts.dart';
import './providers/auth.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        ChangeNotifierProvider(
          create: (ctx) => Auth(),
        ),
        ChangeNotifierProxyProvider<Auth, Contracts>(
          update: (ctx, auth, previousProducts) => Contracts(
              auth.userWalletAddress,
              previousProducts == null ? [] : previousProducts.items),
          create: null,
        ),
      ],
      child: Consumer<Auth>(
        builder: ((ctx, auth, _) => MaterialApp(
              debugShowCheckedModeBanner: false,
              title: 'Dashboard',
              theme: ThemeData(
                canvasColor: Color.fromRGBO(37, 37, 37, 1).withOpacity(0.9),
                brightness: Brightness.dark,
                primarySwatch: Colors.deepOrange,
                accentColor: Colors.cyan,
                fontFamily: 'Lato',
              ),
              // themeMode: ThemeMode.dark,
              home: auth.isAuth ? ContractsOverviewScreen() : AuthScreen(),
              routes: {
                // ContractDetailScreen.routeName: (ctx) => ContractDetailScreen(),
                ViewContractScreen.routeName: (ctx) => ViewContractScreen(),
                EditContractScreen.routeName: (ctx) => EditContractScreen(),
                JudgeDutyScreen.routeName: (ctx) => JudgeDutyScreen(),
              },
            )),
      ),
    );
    //  IMPORTANT! - When using providers - always define them in the highest
    //  point in the app
  }
}
