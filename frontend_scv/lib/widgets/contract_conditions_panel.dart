import 'package:flutter/material.dart';
import 'package:unison/services/Server/negotiationService.dart';
import 'package:unison/widgets/condition_item.dart';

import '../models/contract.dart';

class ContractConditionsPanel extends StatelessWidget {
  final Contract _contract;
  ContractConditionsPanel(this._contract);
  @override
  Widget build(BuildContext context) {
    NegotiationService negS = NegotiationService();
    return Container(
      child: _contract.conditions.isEmpty //TODO handle empty conditions
          ? Text('No Conditions Set!')
          : ListView.builder(
              itemCount: _contract.conditions.length,
              itemBuilder: (_, i) => Column(
                children: [
                  // Text(_contract.conditions[i].description),
                  ConditionItem(
                    contractCondition: _contract.conditions[i],
                    negotiationService: negS,
                  ),
                  Divider(),
                ],
              ),
            ),
    );
  }
}
