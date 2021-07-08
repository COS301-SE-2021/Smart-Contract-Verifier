import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../providers/contracts.dart';
import './contract_item.dart';

class ContractsGrid extends StatelessWidget {
  final bool showFavs;
  ContractsGrid([this.showFavs]);
  @override
  Widget build(BuildContext context) {
    final productsData = Provider.of<Contracts>(context);
    final contracts =
        showFavs ? productsData.favoriteItems : productsData.items;
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
