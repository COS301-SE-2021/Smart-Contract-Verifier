import 'package:flutter/material.dart';
import 'package:unison/models/global.dart';
import 'package:unison/services/Server/judgeService.dart';

import '../models/contract.dart';

class JudgeDetailInfoPanel extends StatefulWidget {
  final Contract _contract;

  JudgeDetailInfoPanel(this._contract);

  @override
  _JudgeDetailInfoPanelState createState() => _JudgeDetailInfoPanelState();
}

class _JudgeDetailInfoPanelState extends State<JudgeDetailInfoPanel> {
  JudgeService _judgeService = JudgeService();

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Card(
        elevation: 15,
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: <Widget>[
            ListTile(
              leading: Icon(Icons.info),
              title: Text(widget._contract.contractId),
              subtitle: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Text('Created: ${widget._contract.createdDate}'),
                  Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      ElevatedButton(
                        onPressed: () {
                          _judgeService.vote(
                              widget._contract.blockchainId, true);
                        },
                        child: Text('Agreement was fulfilled'),
                        style: ElevatedButton.styleFrom(
                          primary: Colors.green,
                          padding: EdgeInsets.symmetric(
                              horizontal: 15, vertical: 10),
                        ),
                      ),
                      SizedBox(
                        height: 10,
                      ),
                      ElevatedButton(
                        onPressed: () {
                          _judgeService.vote(
                              widget._contract.blockchainId, false);
                        },
                        child: Text('Agreement was not fulfilled'),
                        style: ElevatedButton.styleFrom(
                          primary: Colors.red,
                          padding: EdgeInsets.symmetric(
                              horizontal: 15, vertical: 10),
                        ),
                      ),
                    ],
                  ),
                  SizedBox(
                    width: 15,
                  ),
                ],
              ),
            ),
            Container(
              padding: EdgeInsets.only(
                left: 15,
                right: 15,
                bottom: 10,
              ),
              width: double.infinity,
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text('Agreement ID: ${widget._contract.contractId}'),
                  Text(
                    'Party A: ${widget._contract.partyA}',
                    style: TextStyle(
                      color: widget._contract.partyA == Global.userAddress
                          ? Colors.deepOrange
                          : Colors.cyan,
                    ),
                  ),
                  Text(
                    'Party B: ${widget._contract.partyB}',
                    style: TextStyle(
                      color: widget._contract.partyB == Global.userAddress
                          ? Colors.deepOrange
                          : Colors.cyan,
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
