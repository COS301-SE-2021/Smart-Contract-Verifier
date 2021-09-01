import 'package:awesome_loader/awesome_loader.dart';
import 'package:flutter/material.dart';
import 'package:unison/models/blockchainAgreement.dart';
import 'package:unison/models/contract.dart';
import 'package:unison/models/global.dart';
import 'package:unison/services/Blockchain/unisonService.dart';
import 'package:unison/services/Server/commonService.dart';
import 'package:unison/services/Server/negotiationService.dart';
import 'package:unison/widgets/waiting_for_blockchain_text.dart';

class ContractActionArea extends StatefulWidget {
  Contract _agreement;
  final NegotiationService _negotiationService;
  final UnisonService _unisonService;
  final Function _reloadParent;

  final CommonService _commonService = CommonService();

  ContractActionArea(
    this._agreement,
    this._negotiationService,
    this._unisonService,
    this._reloadParent,
  );

  @override
  _ContractActionAreaState createState() => _ContractActionAreaState();
}

class _ContractActionAreaState extends State<ContractActionArea> {
  bool _isLoading = false;
  bool _disabled = false;

  showLoaderDialog(BuildContext context) {
    AlertDialog alert = AlertDialog(
      content: new Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          AwesomeLoader(
            loaderType: AwesomeLoader.AwesomeLoader4,
            color: Colors.teal,
          ),
          Container(
              margin: EdgeInsets.symmetric(vertical: 10, horizontal: 20),
              child: WaitingForBlockChainText()),
        ],
      ),
    );
    showDialog(
      barrierDismissible: false,
      context: context,
      builder: (BuildContext context) {
        return alert;
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    //if seal agreement button must be shown or not:
    if (_isLoading) {
      return AwesomeLoader(
        loaderType: AwesomeLoader.AwesomeLoader4,
        color: Colors.pink,
      );
    }

    if (widget._agreement.movedToBlockchain == false ||
        widget._agreement.blockchainId == BigInt.from(-1)) {
      //Show SEAL button:
      return TextButton(
        onPressed: _disabled
            ? () {
                // print('Already Pressed');
              }
            : () async {
                // print('Press Trigger');
                showLoaderDialog(context);
                try {
                  await widget._negotiationService
                      .sealAgreement(widget._agreement);
                  AgreementState as;
                  bool valid = false;
                  while (!valid) {
                    // print('in while');
                    await Future.delayed(Duration(seconds: 2));
                    widget._agreement = await widget._commonService
                        .getAgreement(widget._agreement.contractId);
                    if (widget._agreement.blockchainId == null ||
                        widget._agreement.blockchainId == BigInt.from(-1)) {
                      print('No Blockchain ID yet...');
                    } else {
                      print('Valid Blockchain ID detected...');

                      as = (await widget._unisonService
                              .getAgreement(widget._agreement.blockchainId))
                          .getAgreementState();
                      print(as.toString());
                      if (as != AgreementState.PENDING) {
                        valid = true;
                      }
                    }
                  }
                  Navigator.of(context).pop();
                  setState(() {});
                } catch (error) {
                  //TODO: make dialog throw handler in parent
                  Navigator.of(context).pop();
                  setState(() {});
                }
              },
        child: Text('Seal Agreement'),
      );
    }

    return FutureBuilder(
      future:
          widget._unisonService.getAgreement(widget._agreement.blockchainId),
      builder: (context, bcAgreementSnap) {
        if (bcAgreementSnap.connectionState != ConnectionState.done) {
          return AwesomeLoader(
            loaderType: AwesomeLoader.AwesomeLoader4,
            color: Colors.teal,
          );
        }

        BlockchainAgreement _loadedBCAgreement = bcAgreementSnap.data;

        if (_loadedBCAgreement == null) {
          print(widget._agreement.blockchainId.toString());
          print('CONN STATE: BCA Snap: ' +
              bcAgreementSnap.connectionState.toString());
          // setState(() {});
          return Text('Oops - Something went wrong'); //TODO: Blockchain ID
          // does not persist
        }
        AgreementState currentState = _loadedBCAgreement.getAgreementState();

        if (currentState == AgreementState.PROPOSED) {
          //Check if the current user needs to Accept move to Blockchain:
          if (_loadedBCAgreement.shouldAccept()) {
            return Text('Show ACCEPT MOVE TO BC button');
          }
          return Text('Awaiting acceptance from other party');
        }

        return Text(currentState.toString());
      },
    );
  }
}
