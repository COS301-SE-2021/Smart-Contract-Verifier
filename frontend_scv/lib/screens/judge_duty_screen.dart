import 'package:flutter/material.dart';
import 'package:unison/widgets/judge_active_cases_grid.dart';
import 'package:provider/provider.dart';
import '../providers/auth.dart';
import '../widgets/app_drawer.dart';

class JudgeDutyScreen extends StatelessWidget {
  static const routeName = '/judge-duty';
  @override
  Widget build(BuildContext context) {
    final userAddress = Provider.of<Auth>(context).userWalletAddress;

    Future<void> _print(String message) async {
      return showDialog<void>(
        context: context,
        barrierDismissible: false, // user must tap button!
        builder: (BuildContext context) {
          return AlertDialog(
            title: Text('Debug'),
            content: Text(message),
            actions: <Widget>[
              TextButton(
                child: const Text('Approve'),
                onPressed: () {
                  Navigator.of(context).pop();
                },
              ),
            ],
          );
        },
      );
    }

    _print(userAddress);

    return Scaffold(
      appBar: AppBar(
        title: Text('Judge Duty'),
      ),
      drawer: AppDrawer(),
      body: Column(
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
          Expanded(child: JudgeActiveCasesGrid()),
        ],
      ),
    );
  }
}
