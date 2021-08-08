import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../providers/contracts.dart';
import './contract_item.dart';

class ContractsGrid extends StatelessWidget {
  // final bool showFavs;
  ContractsGrid();
  @override
  Widget build(BuildContext context) {
    //-> setting/getting contracts happens in the contracts_overview_screen
    final contracts = Provider.of<Contracts>(context).items; //simply access
    // the items here
    print(
        '\n\n_______________GRID contracts[0]__\n\n${contracts[0].toString()}\n'
        '\n_________________\n\n');
    return ListView.builder(
      padding: const EdgeInsets.all(10.0),
      itemCount: contracts.length,
      itemBuilder: (ctx, i) => ChangeNotifierProvider.value(
        value: contracts[i],
        child: ContractItem(),
      ),
    );
  }
}
