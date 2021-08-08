import 'package:flutter/material.dart';
import '../providers/contract.dart';

class ContractDetailInfoPanel extends StatelessWidget {
  final Contract _contract;

  ContractDetailInfoPanel(this._contract);

  @override
  Widget build(BuildContext context) {
    print('\n\n_______________Detail__\n\n${_contract.contractId}\n'
        '\n_________________\n\n');
    return Center(
      child: Card(
        elevation: 15,
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: <Widget>[
            ListTile(
              leading: Icon(Icons.info),
              title: Text(_contract.contractId),
              subtitle: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Text('Created: ${_contract.createdDate}'),
                  Text(
                      'Moved to Blockchain: ${_contract.movedToBlockchain.toString() == 'null' ? 'false' : _contract.movedToBlockchain.toString()}'),
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
                  Text('Agreement ID: ${_contract.contractId}'),
                  Text('Party A: ${_contract.partyA}'),
                  Text('Party B: ${_contract.partyB}'),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
