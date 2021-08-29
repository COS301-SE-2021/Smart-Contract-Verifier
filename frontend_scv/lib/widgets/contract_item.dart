import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:unison/widgets/jdenticon_svg.dart';

import '../models/contract.dart';
import '../screens/view_contract_screen.dart';

class ContractItem extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    final contract = Provider.of<Contract>(context);
    return GestureDetector(
      onTap: () {
        Navigator.of(context).pushNamed(
          ViewContractScreen.routeName,
          arguments: contract.contractId,
        );
      },
      child: ListTile(
        title: Text(contract.title),
        leading: JdenticonSVG(contract.contractId, '#ff7800'),
      ),
    );
  }
}
