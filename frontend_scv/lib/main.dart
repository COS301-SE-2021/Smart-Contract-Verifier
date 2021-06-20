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
        '0x743Fb032c0bE976e1178d8157f911a9e825d9E23')
  ];
  static List<Term> dummyTermsB = [
    Term(
        '0',
        'The car must be blue',
        'Currently the car is red and I want that red paint please - it is '
            'crucial that the paint is red',
        TermStatus.Pending,
        '0x743Fb032c0bE976e1178d8157f911a9e825d9E23'),
    Term(
        '1',
        'The car must be given a service',
        'Proof of a service from a qualified technician is required',
        TermStatus.Pending,
        '0x743Fb032c0bE976e1178d8157f911a9e825d9E23'),
  ];
  final agreementA = Contract(
    id: 'a967075d-d406-4cd6-b57f-8deb18940bf7',
    terms: dummyTermsA,
    status: ContractStatus.Negotiation,
    partyA: '0x743Fb032c0bE976e1178d8157f911a9e825d9E23',
    partyB: '0x37Ec9a8aBFa094b24054422564e68B08aF3114B4',
    createdDate: "2021-06-19T23:47:05.454+00:00",
    movedToBlockchain: true,
    duration: '',
    sealedDate: '',
  );

  final agreementB = Contract(
    id: '8deb18940bf7-4cd6-d406-b57f-a967075d',
    terms: dummyTermsB,
    status: ContractStatus.Negotiation,
    partyA: '0x743Fb032c0bE976e1178d8157f911a9e825d9E23',
    partyB: '0x37Ec9a8aBFa094b24054422564e68B08aF3114B4',
    createdDate: "2021-06-20T23:47:05.454+00:00",
    movedToBlockchain: false,
    duration: '',
    sealedDate: '',
  );

  @override
  Widget build(BuildContext context) {
    List<Contract> _contracts = [agreementA, agreementB];

    return MaterialApp(
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
