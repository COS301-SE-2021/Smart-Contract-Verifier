import 'package:flutter/material.dart';
// import 'package:flutter/foundation.dart' show kIsWeb;
import '../widgets/app_drawer.dart';
import '../widgets/contracts_grid.dart';

class JudgeDutyScreen extends StatelessWidget {
  static const routeName = '/judge-duty';
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Judge Duty'),
      ),
      drawer: AppDrawer(),
      body: Center(
        child: ContractsGrid(),
      ),
    );
  }
}
