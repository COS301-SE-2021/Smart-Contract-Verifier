import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import './contract_item.dart';
import '../models/contracts.dart';

class ContractsGrid extends StatelessWidget {
  // final bool showFavs;
  ContractsGrid();
  @override
  Widget build(BuildContext context) {
    //-> setting/getting contracts happens in the contracts_overview_screen
    final contracts = Provider.of<Contracts>(context).items; //simply access
    var _firstUser = false;
    if (contracts == null) {
      _firstUser = true;
    }

    return ListView.builder(
      padding: const EdgeInsets.all(10.0),
      itemCount: _firstUser ? 0 : contracts.length,
      itemBuilder: (ctx, i) => ChangeNotifierProvider.value(
        value: contracts[i],
        child: ContractItem(),
      ),
    );
  }
}
