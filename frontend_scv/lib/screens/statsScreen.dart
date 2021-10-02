//This screen shows statistical data about Unison

import 'package:awesome_loader/awesome_loader.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:unison/models/generalStats.dart';
import 'package:unison/services/Server/statsService.dart';
import 'package:unison/widgets/miscellaneous/dashboard_actions.dart';
import 'package:unison/widgets/miscellaneous/funky_text_widget.dart';

import '../models/contracts.dart';
import '../screens/edit_contract_screen.dart';
import '../widgets/miscellaneous/app_drawer.dart';
import '../widgets/agreement/contracts_grid.dart';

class StatsScreen extends StatelessWidget {
  static const routeName = '/view-stats';
  StatsService _statServ = StatsService();

  //This returns an appropriate widget after getting some statistics
  Future<Widget> getStats() async {
    GeneralStats gS;

    try {
      gS = await _statServ.getGeneralStats();
    }
    catch (e) {
      return Text(e);
    }

    return Text('Total Number of Users: ' + gS.totalUsers.toString());
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: FunkyText('Unison Statistics'),
          actions: [
            DashBoardActions(),
          ],
        ),
        drawer: AppDrawer(),
        body: FutureBuilder(future: getStats(), builder: (context, snap) {
          return snap.connectionState == ConnectionState.done
              ? snap.data
              : AwesomeLoader();
        },));
  }
}