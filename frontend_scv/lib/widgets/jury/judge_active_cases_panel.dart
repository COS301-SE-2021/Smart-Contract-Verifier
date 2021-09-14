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

  @override
  Widget build(BuildContext context) {
    JudgeService judgeService = JudgeService();
    return FutureBuilder(
      future: judgeService.getInvolvedAgreements(),
      builder: (context, agreementsSnapshot) {
        return agreementsSnapshot.connectionState != ConnectionState.done
            ? CircularProgressIndicator()
            : agreementsSnapshot.data.length == 0
                ? Text('You have no Assignments')
                : ListView.builder(
                    padding: const EdgeInsets.all(10.0),
                    itemCount: agreementsSnapshot.data.length,
                    itemBuilder: (BuildContext context, int index) {
                      final agreement = agreementsSnapshot.data[index];
                      return JudgeActiveContractItem(agreement);
                    },
                  );
      },
    );
  }
}
