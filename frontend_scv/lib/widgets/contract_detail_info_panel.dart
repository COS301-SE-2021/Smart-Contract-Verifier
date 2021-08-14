import 'package:flutter/material.dart';
import 'package:unison/models/condition.dart';
import 'package:unison/models/global.dart';
import 'package:unison/services/Server/negotiationService.dart';

import '../models/contract.dart';

class ContractDetailInfoPanel extends StatefulWidget {
  final Contract _contract;
  ContractDetailInfoPanel(this._contract);
  NegotiationService negotiationService = NegotiationService();

  @override
  _ContractDetailInfoPanelState createState() =>
      _ContractDetailInfoPanelState();
}

class _ContractDetailInfoPanelState extends State<ContractDetailInfoPanel> {
  bool disableSealButton;
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
                  Text(
                      'Moved to Blockchain: ${widget._contract.movedToBlockchain.toString() == 'null' ? 'false' : widget._contract.movedToBlockchain.toString()}'),
                  SizedBox(
                    width: 15,
                  ),
                  _buildSealButton(),
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

  Widget _buildSealButton() {
    bool stillPending = false;
    widget._contract.conditions.forEach((condition) {
      if (condition.status == 'PENDING') {
        stillPending = true;
      }
    });

    return stillPending
        ? Text('Waiting for PENDING conditions')
        : ElevatedButton(
            onPressed: () async {
              await widget.negotiationService.sealAgreement(widget._contract);
              //TODO setState
              print('SEAL THAT DEAL');
            },
            child: Text('Seal Agreement'),
          );
  }
}
