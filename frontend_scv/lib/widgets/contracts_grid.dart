import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../providers/contracts.dart';
import './contract_item.dart';

class ContractsGrid extends StatelessWidget {
  final bool showFavs;
  ContractsGrid([this.showFavs]);
  @override
  Widget build(BuildContext context) {
    final contractsData = Provider.of<Contracts>(context);
    final contracts =
        showFavs ? contractsData.favoriteItems : contractsData.items;
    Future<void> _print(String message) async {
      return showDialog<void>(
        context: context,
        barrierDismissible: false, // user must tap button!
        builder: (BuildContext context) {
          return AlertDialog(
            title: Text('Debug'),
            content: Text(message),
            actions: <Widget>[
              TextButton(
                child: const Text('Approve'),
                onPressed: () {
                  Navigator.of(context).pop();
                },
              ),
            ],
          );
        },
      );
    }

    _print(contracts.length.toString() + contracts[0].description);

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
