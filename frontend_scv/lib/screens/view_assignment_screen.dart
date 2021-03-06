import 'package:flutter/material.dart';
import 'package:unison/models/contract.dart';
import 'package:unison/screens/messaging_screen.dart';
import 'package:unison/services/Server/negotiationService.dart';
import 'package:unison/widgets/jury/judge_detail_info_panel.dart';
import 'package:unison/widgets/jury/judge_conditions_panel.dart';
import 'package:unison/widgets/miscellaneous/funky_text_widget.dart';

import 'evidence_screen.dart';

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
        title: FunkyText('Current Assignment'),
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
            child: Container(),
          )
        ],
      ),
      floatingActionButtonLocation: FloatingActionButtonLocation.endFloat,
      floatingActionButton: Column(
        mainAxisAlignment: MainAxisAlignment.end,
        crossAxisAlignment: CrossAxisAlignment.end,
        children: [
          FloatingActionButton.extended(
            onPressed: () {
              Navigator.of(context).pushNamed(
                EvidenceScreen.routeName,
                arguments: {
                  'agreementId': loadedContract.contractId,
                  'partyA': loadedContract.partyA,
                  'partyB': loadedContract.partyB,
                },
              );
            },
            label: Text('Evidence'),
            icon: Icon(Icons.inventory_2),
            backgroundColor: Color.fromRGBO(50, 183, 196, 1),
          ),
          SizedBox(
            height: 15,
          ),
          FloatingActionButton.extended(
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
        ],
      ),
    );
  }
}
