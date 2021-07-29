import 'package:flutter/material.dart';
import 'package:unison/widgets/judge_active_cases_grid.dart';
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
      body: Center(
        child: JudgeActiveCasesGrid(),
      ),
    );
  }
}
