import 'package:flutter/material.dart';
import 'package:flutter/foundation.dart' show kIsWeb;

import '../widgets/main_drawer.dart';
import '../models/contract.dart';

class DashboardScreen extends StatefulWidget {
  final List<Contract> userContracts;

  DashboardScreen(this.userContracts);

  @override
  _DashboardScreenState createState() => _DashboardScreenState();
}

void _addNewContract(String title, double amount, DateTime date) {
  List<Term> dummyTerms = [
    Term.name(
        '1',
        'term text',
        'term description - '
            'normally a bit longer than your average sentence to demonstrate what '
            'this will be used for.',
        TermStatus.Pending,
        'partyA_ID')
  ];
  final newContract = Contract.name(
    'id',
    dummyTerms,
    'Contract Title',
    ContractStatus.Negotiation,
  );
}

void _startAddNewContract(context) {
  print('Start new agreement');
}

class _DashboardScreenState extends State<DashboardScreen> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Dashboard'),
      ),
      drawer: MainDrawer(),
      body: Center(
        child: Text('Dashboard'),
      ),
      floatingActionButtonLocation: kIsWeb
          ? FloatingActionButtonLocation.centerFloat //if is web
          : FloatingActionButtonLocation.endFloat,
      floatingActionButton: kIsWeb
          ? Container(
              margin: EdgeInsets.fromLTRB(0, 0, 0, 120),
              child: FloatingActionButton.extended(
                icon: Icon(Icons.add), // Web
                onPressed: () => _startAddNewContract(context),
                label: const Text('Create New Agreement'),
              ),
            )
          : FloatingActionButton(
              //Mobile
              child: Icon(Icons.add),
              onPressed: () => _startAddNewContract(context),
            ),
    );
  }
}
