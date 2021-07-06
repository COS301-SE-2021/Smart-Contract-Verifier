import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:unison/widgets/contract_detail_info_panel.dart';
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
        body: Center(
          child: ContractDetailInfoPanel(loadedContract),
        ));
  }
}
