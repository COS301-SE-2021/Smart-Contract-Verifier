import 'package:flutter/material.dart';
import 'package:unison/effects/glow_on_hover.dart';
import 'package:unison/screens/view_assignment_screen.dart';
import 'package:unison/widgets/miscellaneous/jdenticon_svg.dart';

import '../../models/contract.dart';

class JudgeActiveContractItem extends StatelessWidget {
  final Contract agreement;

  JudgeActiveContractItem(this.agreement);

  @override
  Widget build(BuildContext context) {
    Widget assignmentItem = GestureDetector(
      onTap: () {
        Navigator.of(context).pushNamed(
          ViewAssignmentScreen.routeName,
          arguments: agreement,
        );
      },
      child: Card(
        color: Color.fromRGBO(56, 61, 81, 1),
        elevation: 10,
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
      ),
    );

    return GlowOnHover(assignmentItem, true);
  }
}
