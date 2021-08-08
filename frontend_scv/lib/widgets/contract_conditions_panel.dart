import 'package:flutter/material.dart';
import 'package:unison/widgets/condition_item.dart';
import '../providers/contract.dart';

class ContractConditionsPanel extends StatelessWidget {
  final Contract _contract;

  ContractConditionsPanel(this._contract);

  @override
  Widget build(BuildContext context) {
    print(_contract.conditions);
    return Container(
      // padding: EdgeInsets.all(8),
      child: _contract.conditions.length == 1 //TODO handle empty conditions
          ? Text('No Conditions Set!')
          : ListView.builder(
              itemCount: _contract.conditions.length,
              itemBuilder: (_, i) => Column(
                children: [
                  ConditionItem(contractCondition: _contract.conditions[i]),
                  Divider(),
                ],
              ),
            ),
    );
  }
}