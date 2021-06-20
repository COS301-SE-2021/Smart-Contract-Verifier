import 'package:flutter/material.dart';
import 'package:frontend_scv/screens/contract_detail_screen.dart';
import './screens/dashboard_screen.dart';
import './screens/settings_screen.dart';
import './models/contract.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  static List<Term> dummyTermsA = [
    Term(
        '1',
        'term text',
        'term description - '
            'normally a bit longer than your average sentence to demonstrate what '
            'this will be used for.',
        TermStatus.Pending,
        'partyA_ID')
  ];
  static List<Term> dummyTermsB = [
    Term(
        '1',
        'term text',
        'term description - '
            'normally a bit longer than your average sentence to demonstrate what '
            'this will be used for.',
        TermStatus.Pending,
        'partyA_ID')
  ];
  final agreementA = Contract(
    id: 'a967075d-d406-4cd6-b57f-8deb18940bf7',
    terms: dummyTermsA,
    status: ContractStatus.Negotiation,
    partyA: 'par-tyA-IDAgreent01',
    partyB: 'par-tyB-IDAgreent01',
  );

  // final agreementB = Contract(
  //   'a9987079d-d416-d406-d509-8ubn18670b69',
  //   dummyTermsB,
  //   ContractStatus.Sealed,
  //   'par-tyA-IDAgreent02',
  //   'par-tyB-IDAgreent02',
  // );

  @override
  Widget build(BuildContext context) {
    List<Contract> _contracts = [agreementA];

    return MaterialApp(
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
          '/': (ctx) => DashboardScreen(_contracts),
          SettingsScreen.routeName: (ctx) => SettingsScreen(),
          // ContractDetailScreen.routeName: (ctx) => ContractDetailScreen(),
        },
        onGenerateRoute: (settings) {
          // If you push the PassArguments route
          if (settings.name == ContractDetailScreen.routeName) {
            // Cast the arguments to the correct
            // type: ScreenArguments.
            final args = settings.arguments as Contract;

            // Then, extract the required data from
            // the arguments and pass the data to the
            // correct screen.
            return MaterialPageRoute(
              builder: (context) {
                return ContractDetailScreen(args);
              },
            );
          }
        });
  }
}
