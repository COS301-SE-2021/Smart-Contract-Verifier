import 'package:flutter/material.dart';
import './contract_item.dart';
import 'package:provider/provider.dart';
import '../providers/contracts.dart';

class ContractsGrid extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    //Access Provider
    final contractsData = Provider.of<Contracts>(context);
    final contracts = contractsData.contracts;
    //////////////////
    return GridView.builder(
      padding: const EdgeInsets.all(10.0),
      itemBuilder: (ctx, index) => ContractItem(
        contracts[index].id,
        contracts[index].status.toString(),
      ),
      gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
        crossAxisCount: 1,
        childAspectRatio: 3 / 2,
        crossAxisSpacing: 10,
        mainAxisSpacing: 10,
      ),
      itemCount: contracts.length,
    );
  }
}
