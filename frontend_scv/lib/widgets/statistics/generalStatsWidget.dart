//This widget displays some general stats about Unison.
//Is is meant to be placed on the stat screen.

//All data can be formatted here
//TODO: Have fun Kevin

import 'package:flutter/cupertino.dart';
import 'package:unison/models/generalStats.dart';

class GeneralStatsWidget extends StatelessWidget {

  final GeneralStats _stats;
  GeneralStatsWidget(this._stats);

  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(borderRadius: BorderRadius.circular(24.0),
        color: const Color(0xff2c2c2c),),
      child: Row(
        children: [
          Column(
            children: [
              Text('Total number of users: ' + _stats.totalUsers.toString()),
              Text('Judges: ' + _stats.numJudges.toString()),
              Text('Agreements: ' + _stats.numAgreements.toString())
            ],
          ),
          Column(
            children: [
              Text('Agreements sealed: ' + _stats.numSealed.toString()),
              Text('Agreements concluded: ' + _stats.concluded.toString()),
              Text('Agreements disputed: ' + _stats.disputed.toString()),
              Text('Average negotiation period: ' + _stats.averageNegPeriod.toString()),
            ],
          )
        ],
      ),
    );
  }
}