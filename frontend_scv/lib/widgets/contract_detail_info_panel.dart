import 'package:flutter/material.dart';
import 'package:unison/services/Blockchain/unisonService.dart';
import 'package:unison/services/Server/negotiationService.dart';
import 'package:unison/widgets/contract_action_area.dart';
import 'package:unison/widgets/jdenticon_svg.dart';

import '../models/contract.dart';

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

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Card(
        color: Color.fromRGBO(56, 61, 81, 1),
        elevation: 15,
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: <Widget>[
            ListTile(
              leading: JdenticonSVG(widget._contract.contractId, [205]),
              title: Text(widget._contract.title),
              subtitle: Text(widget._contract.description),
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
                  // TODO: Add Contract Details (ID's etc.)
                ],
              ),
            ),
          ],
        ),
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
    if (widget._contract.price == null) {
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
