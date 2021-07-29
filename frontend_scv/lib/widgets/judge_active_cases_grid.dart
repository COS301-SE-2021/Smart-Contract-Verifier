import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:unison/widgets/judge_active_contract_item.dart';
import '../providers/contracts.dart';

class JudgeActiveCasesGrid extends StatelessWidget {
  JudgeActiveCasesGrid();
  @override
  Widget build(BuildContext context) {
    final contractsData = Provider.of<Contracts>(context);
    final contracts = contractsData.items;
    return ListView.builder(
      padding: const EdgeInsets.all(10.0),
      itemCount: contracts.length,
      itemBuilder: (ctx, i) => ChangeNotifierProvider.value(
        value: contracts[i],
        child: JudgeActiveContractItem(),
      ),
    );
  }
}
