import 'package:flutter/material.dart';
import 'package:flutter/foundation.dart' show kIsWeb;
import '../widgets/app_drawer.dart';
import '../widgets/contracts_grid.dart';
import 'package:provider/provider.dart';
import '../providers/auth.dart';

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
