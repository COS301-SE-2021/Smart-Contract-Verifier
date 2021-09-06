import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:unison/models/condition.dart';
import 'package:unison/models/contract.dart';
import 'package:unison/models/global.dart';
import 'package:unison/screens/messaging_screen.dart';
import 'package:unison/services/Server/negotiationService.dart';
import 'package:unison/widgets/JudgeDetailInfoPanel.dart';
import 'package:unison/widgets/judge_conditions_panel.dart';

import '../models/contracts.dart';
import '../widgets/contract_conditions_panel.dart';
import '../widgets/contract_detail_info_panel.dart';

class ViewAssignmentScreen extends StatefulWidget {
  static const routeName = '/view-assignment';

  @override
  _ViewAssignmentScreenState createState() => _ViewAssignmentScreenState();
}

enum ConditionType { Normal, Payment, Duration }

class _ViewAssignmentScreenState extends State<ViewAssignmentScreen> {
  var _isLoading = false;
  NegotiationService negotiationService = NegotiationService();
  @override
  void initState() {
    super.initState();
  }

  Widget build(BuildContext context) {
    final loadedContract =
        ModalRoute.of(context).settings.arguments as Contract;
    return Scaffold(
      appBar: AppBar(
        title: Text(loadedContract.title),
      ),
      body: Column(
        children: [
          JudgeDetailInfoPanel(loadedContract),
          Container(
            padding: EdgeInsets.symmetric(
              vertical: 10,
              horizontal: 8,
            ),
            // alignment: Alignment.centerLeft,
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text(
                  'Agreement Conditions:',
                  style: TextStyle(
                    fontSize: 16,
                  ),
                ),
              ],
            ),
          ),
          Expanded(
            child: _isLoading
                ? Center(
                    child: CircularProgressIndicator(),
                  )
                : JudgeConditionsPanel(loadedContract),
            flex: 6,
          ),
          Expanded(
            flex: 1,
            child: Container(
                // TODO: More Information or actions here?
                ),
          )
        ],
      ),
      floatingActionButtonLocation: FloatingActionButtonLocation.endFloat,
      floatingActionButton: FloatingActionButton.extended(
        onPressed: () {
          Navigator.of(context).pushNamed(
            MessagingScreen.routeName,
            arguments: {
              'agreementId': loadedContract.contractId,
              'partyA': loadedContract.partyA,
              'partyB': loadedContract.partyB,
            },
          );
        },
        label: Text('Agreement Chat'),
        icon: Icon(Icons.chat),
        backgroundColor: Color.fromRGBO(182, 80, 158, 1),
      ),
    );
  }
}
