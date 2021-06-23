import 'package:flutter/material.dart';
import 'package:flutter/foundation.dart' show kIsWeb;
import '../widgets/contracts_grid.dart';

import '../widgets/main_drawer.dart';

class DashboardScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Dashboard'),
      ),
      drawer: MainDrawer(),
      body: Center(
        child: ContractsGrid(),
      ),
      // floatingActionButtonLocation: kIsWeb
      //     ? FloatingActionButtonLocation.centerFloat //if is web
      //     : FloatingActionButtonLocation.endFloat,
      // floatingActionButton: kIsWeb
      //     ? Container(
      //         margin: EdgeInsets.fromLTRB(0, 0, 0, 120),
      //         child: FloatingActionButton.extended(
      //           icon: Icon(Icons.add), // Web
      //           onPressed: () => _startAddNewContract(context),
      //           label: const Text('Create New Agreement'),
      //         ),
      //       )
      //     : FloatingActionButton(
      //         //Mobile
      //         child: Icon(Icons.add),
      //         onPressed: () => _startAddNewContract(context),
      //       ),
    );
  }
}
