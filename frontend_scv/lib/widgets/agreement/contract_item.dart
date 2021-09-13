import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:unison/effects/glow_on_hover.dart';
import 'package:unison/widgets/miscellaneous/jdenticon_svg.dart';

import '../../models/contract.dart';
import '../../screens/view_contract_screen.dart';

class ContractItem extends StatefulWidget {
  @override
  _ContractItemState createState() => _ContractItemState();
}

class _ContractItemState extends State<ContractItem> {
  @override
  Widget build(BuildContext context) {
    final contract = Provider.of<Contract>(context);

    Widget agreementItem = GestureDetector(
      onTap: () {
        Navigator.of(context).pushNamed(
          ViewContractScreen.routeName,
          arguments: contract.contractId,
        );
      },
      child: Card(
        color: Color.fromRGBO(56, 61, 81, 1),
        elevation: 10,
        child: ListTile(
          title: Text(contract.title),
          leading: JdenticonSVG(contract.contractId, [205]), //205
        ),
      ),
    );

    return GlowOnHover(agreementItem);
  }
}
