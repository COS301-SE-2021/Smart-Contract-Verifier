import 'package:flutter/material.dart';
import 'package:unison/models/global.dart';
import 'package:unison/services/Server/judgeService.dart';
import 'package:unison/widgets/judge_active_cases_panel.dart';

import '../widgets/app_drawer.dart';

class JudgeDutyScreen extends StatelessWidget {
  static const routeName = '/judge-duty';
  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text('Judge Duty'),
        ),
        drawer: AppDrawer(),
        body: Global.isJudge
            ? Column(
                children: [
                  SizedBox(
                    height: 20,
                  ),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.start,
                    children: [
                      SizedBox(
                        width: 20,
                      ),
                      Text(
                        'Current Assignments:',
                        style: TextStyle(
                          fontSize: 16,
                          decoration: TextDecoration.underline,
                        ),
                      ),
                    ],
                  ),
                  Expanded(child: JudgeActiveCasesPanel()),
                ],
              )
            : Center(
                child: Column(
                  children: [
                    SizedBox(
                      height: 20,
                    ),
                    Text(
                      'You are currently not a judge. You can up to become'
                      ' a judge with the button below:',
                      style: TextStyle(fontSize: 18),
                    ),
                    SizedBox(
                      height: 20,
                    ),
                    ElevatedButton(
                        onPressed: () async {
                          JudgeService jS = JudgeService();
                          await jS.makeUserJudge();
                          // Global.isJudge = true;
                          Navigator.of(context).pushNamed(
                            JudgeDutyScreen.routeName,
                          );
                        },
                        child: Text('Become a Judge'))
                  ],
                ),
              ));
  }
}
