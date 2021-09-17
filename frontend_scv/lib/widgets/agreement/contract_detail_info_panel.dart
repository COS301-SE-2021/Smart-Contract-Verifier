import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:unison/services/Blockchain/unisonService.dart';
import 'package:unison/services/Server/negotiationService.dart';
import 'package:unison/widgets/agreement/contract_action_area.dart';
import 'package:unison/widgets/miscellaneous/jdenticon_svg.dart';

import '../../models/contract.dart';

class ContractDetailInfoPanel extends StatefulWidget {
  final Contract _contract;
  ContractDetailInfoPanel(this._contract);
  final NegotiationService negotiationService = NegotiationService();
  final UnisonService unisonService = UnisonService();
  bool _sealable = false;

  @override
  _ContractDetailInfoPanelState createState() =>
      _ContractDetailInfoPanelState();
}

class _ContractDetailInfoPanelState extends State<ContractDetailInfoPanel> {
  void _reloadView() {
    setState(() {});
  }

  final snackBar = SnackBar(
    backgroundColor: Color.fromRGBO(56, 61, 81, 1),
    content: Row(mainAxisAlignment: MainAxisAlignment.center, children: [
      Text(
        'Address Copied to Clipboard',
        style: TextStyle(color: Colors.pink, fontSize: 16),
      )
    ]),
  );
  @override
  Widget build(BuildContext context) {
    return Card(
      color: Color.fromRGBO(56, 61, 81, 1),
      elevation: 15,
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: <Widget>[
          ListTile(
            leading: JdenticonSVG(widget._contract.contractId, [205]),
            title: Text(widget._contract.title),
            subtitle: Column(
              children: [
                Align(
                    alignment: Alignment.centerLeft,
                    child: Text(widget._contract.description)),
                Row(
                  mainAxisAlignment: MainAxisAlignment.start,
                  children: [
                    Text(
                      'Party A: ${widget._contract.partyA.substring(0, 6)}'
                      '...${widget._contract.partyA.substring(widget._contract.partyA.length - 4, widget._contract.partyA.length)}',
                      style: TextStyle(
                        fontSize: 10,
                      ),
                    ),
                    IconButton(
                      onPressed: () async {
                        Clipboard.setData(
                          ClipboardData(text: widget._contract.partyA),
                        );
                        ScaffoldMessenger.of(context).showSnackBar(snackBar);
                      },
                      icon: Icon(
                        Icons.copy,
                        color: Colors.grey,
                      ),
                    ),
                    Text(
                      'Party B: ${widget._contract.partyB.substring(0, 6)}'
                      '...${widget._contract.partyB.substring(widget._contract.partyB.length - 4, widget._contract.partyB.length)}',
                      style: TextStyle(
                        fontSize: 10,
                      ),
                    ),
                    IconButton(
                      onPressed: () async {
                        Clipboard.setData(
                          ClipboardData(text: widget._contract.partyB),
                        );
                        ScaffoldMessenger.of(context).showSnackBar(snackBar);
                      },
                      icon: Icon(
                        Icons.copy,
                        color: Colors.grey,
                      ),
                    ),
                  ],
                )
              ],
            ),
            isThreeLine: true,
            trailing: Column(
              crossAxisAlignment: CrossAxisAlignment.end,
              children: [
                Text(
                  'Agreement ID: ${widget._contract.contractId}',
                ),
                Text(
                  'Creation Date: ${widget._contract.createdDate}',
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
            child: Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                _buildConditionsMessage(),
                if (widget._sealable)
                  ContractActionArea(
                    widget._contract,
                    widget.negotiationService,
                    widget.unisonService,
                    _reloadView,
                  ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildConditionsMessage() {
    bool stillPending = false;
    String pendingMessage = 'Awaiting PENDING Conditions';
    widget._contract.conditions.forEach((condition) {
      if (condition.status == 'PENDING') {
        stillPending = true;
      }
    });
    if (widget._contract.conditions.isEmpty) {
      stillPending = true;
      pendingMessage = 'Please Add Conditions';
    }
    if (widget._contract.duration == null) {
      stillPending = true;
      pendingMessage = 'Please Add a Duration Condition';
    }
    if (widget._contract.paymentAmount == null) {
      stillPending = true;
      pendingMessage = 'Please Add a Payment Condition';
    }
    if (!stillPending) {
      setState(() {
        widget._sealable = true;
      });
    }
    return stillPending ? Text(pendingMessage) : Container();
  }
}
