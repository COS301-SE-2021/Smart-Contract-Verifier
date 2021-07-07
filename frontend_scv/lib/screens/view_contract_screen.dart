import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../widgets/contract_conditions_panel.dart';
import '../widgets/contract_detail_info_panel.dart';
import '../providers/contracts.dart';
import '../providers/auth.dart';

class ViewContractScreen extends StatelessWidget {
  static const routeName = '/view-contract';

  Widget build(BuildContext context) {
    final contractId = ModalRoute.of(context).settings.arguments as String;
    final loadedContract =
        Provider.of<Contracts>(context, listen: false).findById(contractId);

    return Scaffold(
        appBar: AppBar(
          title: Text(loadedContract.title),
        ),
        body: Column(
          children: [
            ContractDetailInfoPanel(loadedContract),
            Expanded(child: ContractConditionsPanel(loadedContract)),
          ],
        ));
  }
}
