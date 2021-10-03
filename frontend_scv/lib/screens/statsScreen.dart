//This screen shows statistical data about Unison

import 'package:awesome_loader/awesome_loader.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:unison/models/generalStats.dart';
import 'package:unison/services/Server/statsService.dart';
import 'package:unison/widgets/miscellaneous/dashboard_actions.dart';
import 'package:unison/widgets/miscellaneous/errorWidget.dart';
import 'package:unison/widgets/miscellaneous/funky_text_widget.dart';
import 'package:unison/widgets/statistics/generalStatsWidget.dart';

import '../models/contracts.dart';
import '../screens/edit_contract_screen.dart';
import '../widgets/miscellaneous/app_drawer.dart';
import '../widgets/agreement/contracts_grid.dart';

class StatsScreen extends StatelessWidget {
  static const routeName = '/view-stats';
  final StatsService _statServ = StatsService();

  //This returns an appropriate widget after getting some statistics
  Future<Widget> getStats() async {
    GeneralStats gS;
    Widget child;

    try {
      gS = await _statServ.getGeneralStats();
      child = GeneralStatsWidget(gS);
    } catch (e) {
      child = errorWidget(e + ' - Please try again later');
    }

    return Padding(
      padding: const EdgeInsets.all(15),
      child: Container(
          child: Card(
              elevation: 15,
              color: Color.fromRGBO(56, 61, 81, 1),
              child: child)),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: FunkyText('Unison Statistics'),
        ),
        drawer: AppDrawer(),
        body: FutureBuilder(
          future: getStats(),
          builder: (context, snap) {
            return snap.connectionState == ConnectionState.done
                ? snap.data
                : AwesomeLoader();
          },
        ));
  }
}
