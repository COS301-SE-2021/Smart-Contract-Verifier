import 'package:flutter/material.dart';
import 'package:unison/services/Server/negotiationService.dart';
import 'package:unison/widgets/condition_item.dart';
import 'package:unison/widgets/judge_condition_item.dart';

import '../models/contract.dart';

class JudgeConditionsPanel extends StatelessWidget {
  final Contract _contract;

  JudgeConditionsPanel(this._contract);

  @override
  Widget build(BuildContext context) {
    NegotiationService negS = NegotiationService();

    print(_contract.conditions);
    return Container(
      // padding: EdgeInsets.all(8),
      child: _contract.conditions.isEmpty //TODO handle empty conditions
          ? Text('No Conditions Set!')
          : ListView.builder(
              itemCount: _contract.conditions.length,
              itemBuilder: (_, i) => Column(
                children: [
                  // Text(_contract.conditions[i].description),
                  JudgeConditionItem(
                    contractCondition: _contract.conditions[i],
                    party:
                        _contract.partyA == _contract.conditions[i].proposedBy
                            ? 'A'
                            : 'B',
                  ),
                  Divider(),
                ],
              ),
            ),
    );
  }
}
