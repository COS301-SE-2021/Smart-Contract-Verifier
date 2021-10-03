//This widget displays some general stats about Unison.
//Is is meant to be placed on the stat screen.

//All data can be formatted here
//TODO: Have fun Kevin
import 'package:unison/models/generalStats.dart';
import 'package:flutter/material.dart';

class GeneralStatsWidget extends StatelessWidget {
  final GeneralStats _stats;
  GeneralStatsWidget(this._stats);

  Widget build(BuildContext context) {
    return Column(
      mainAxisSize: MainAxisSize.min,
      children: [
        ListTile(
            leading: Icon(
              Icons.people,
              color: Colors.pinkAccent,
            ),
            title: Text('Total number of '
                    'users: ' +
                _stats.totalUsers.toString())),
        ListTile(
            leading: Icon(
              Icons.request_page_outlined,
              color: Colors.pinkAccent,
            ),
            title: Text('Agreements: ' + _stats.numAgreements.toString())),
        ListTile(
            leading: Icon(
              Icons.verified_outlined,
              color: Colors.pinkAccent,
            ),
            title: Text('Agreements sealed: ' + _stats.numSealed.toString())),
        ListTile(
            leading: Icon(
              Icons.military_tech_outlined,
              color: Colors.pinkAccent,
            ),
            title:
                Text('Agreements concluded: ' + _stats.concluded.toString())),
        ListTile(
            leading: Icon(
              Icons.gavel_outlined,
              color: Colors.pinkAccent,
            ),
            title: Text('Agreements disputed: ' + _stats.disputed.toString())),
        ListTile(
            leading: Icon(
              Icons.rule_outlined,
              color: Colors.pinkAccent,
            ),
            title: Text('Average negotiation period: ' +
                _stats.averageNegPeriod.toString())),
      ],
    );
  }
}
