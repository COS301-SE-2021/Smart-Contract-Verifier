import 'package:flutter/material.dart';

import '../widgets/app_drawer.dart';
import '../widgets/contracts_grid.dart';

class DashboardScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Dashboard'),
      ),
      drawer: AppDrawer(),
      body: Center(
        child: ContractsGrid(),
      ),
    );
  }
}
