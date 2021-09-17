import 'package:awesome_loader/awesome_loader.dart';
import 'package:flutter/material.dart';
import 'package:unison/services/Server/judgeService.dart';
import 'package:unison/widgets/jury/judge_active_contract_item.dart';

class JudgeActiveCasesPanel extends StatefulWidget {
  @override
  _JudgeActiveCasesPanelState createState() => _JudgeActiveCasesPanelState();
}

class _JudgeActiveCasesPanelState extends State<JudgeActiveCasesPanel> {
  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    setState(() {});
  }

  ///Attempt to get the involved agreements. If failure, return error widget
  Future<Widget> _tryGetAgreements() async {
    JudgeService judgeService = JudgeService();

    var res;
    try {
      res = await judgeService.getInvolvedAgreements();
      if (res.length == 0) {
        return Text('You have no Assignments');
      }

    } catch (e) {
        return Text(e.toString()); //Data could not be retrieved.
    }

    return ListView.builder(
      padding: const EdgeInsets.all(10.0),
      itemCount: res.length,
      itemBuilder: (BuildContext context, int index) {
        final agreement = res[index];
        return JudgeActiveContractItem(agreement);
      },
    );

  }

  @override
  Widget build(BuildContext context) {

    return FutureBuilder(
      future: _tryGetAgreements(),
      builder: (context, agreementsSnapshot) {
        return agreementsSnapshot.connectionState != ConnectionState.done
            ? AwesomeLoader()
            : agreementsSnapshot.data;
      },
    );
  }
}
