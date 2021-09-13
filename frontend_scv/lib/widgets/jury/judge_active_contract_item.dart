import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:unison/screens/view_assignment_screen.dart';
import 'package:unison/screens/view_contract_screen.dart';
import 'package:unison/widgets/miscellaneous/jdenticon_svg.dart';

// import '../providers/auth.dart';
import '../../models/contract.dart';

class JudgeActiveContractItem extends StatelessWidget {
  final Contract agreement;

  JudgeActiveContractItem(this.agreement);

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () {
        Navigator.of(context).pushNamed(
          ViewAssignmentScreen.routeName,
          arguments: agreement,
        );
      },
      child: ListTile(
        title: Text(agreement.title),
        leading: JdenticonSVG(agreement.contractId, [150]),
        trailing: Container(
          width: 50,
          child: Row(
            children: <Widget>[],
          ),
        ),
      ),
    );
  }
}
