import 'package:flutter/material.dart';
import '../providers/contract.dart';

class ContractConditionsPanel extends StatelessWidget {
  final Contract _contract;

  ContractConditionsPanel(this._contract);

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: EdgeInsets.all(8),
      child: ListView.builder(
        itemCount: _contract.conditions.length,
        itemBuilder: (_, i) => Column(
          children: [
            Text(_contract.conditions[i].toString()),
            Divider(),
          ],
        ),
      ),
    );
  }
}
