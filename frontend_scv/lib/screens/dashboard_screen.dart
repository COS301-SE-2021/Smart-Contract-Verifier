import 'package:flutter/material.dart';
import 'package:flutter/foundation.dart' show kIsWeb;
import 'package:frontend_scv/widgets/contract_list.dart';
import 'package:frontend_scv/widgets/new_contract.dart';

import '../widgets/main_drawer.dart';
import '../models/contract.dart';

class DashboardScreen extends StatefulWidget {
  final List<Contract> userContracts;

  DashboardScreen(this.userContracts);

  @override
  _DashboardScreenState createState() => _DashboardScreenState();
}

void _addNewContract(String title, double amount, DateTime date) {}

void _startAddNewContract(BuildContext ctx) {
  print('Start new agreement');
  showModalBottomSheet(
      context: ctx,
      builder: (_) {
        return NewContract(_addNewContract);
      });
}

class _DashboardScreenState extends State<DashboardScreen> {
  // final List<Contract> contracts = [];

  @override
  Widget build(BuildContext context) {
    print('Hello from build');

    return Scaffold(
      appBar: AppBar(
        title: Text('Dashboard'),
      ),
      drawer: MainDrawer(),
      body: Center(
        child: ContractList(widget.userContracts),
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
