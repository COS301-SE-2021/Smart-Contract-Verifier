import 'package:awesome_loader/awesome_loader.dart';
import 'package:flutter/material.dart';
import 'package:unison/models/blockchainAgreement.dart';
import 'package:unison/models/contract.dart';
import 'package:unison/services/Blockchain/unisonService.dart';
import 'package:unison/services/Server/commonService.dart';
import 'package:unison/services/Server/judgeService.dart';
import 'package:unison/services/Server/negotiationService.dart';
import 'package:unison/widgets/miscellaneous/waiting_for_blockchain_text.dart';

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
  JudgeService _judgeService;

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
    const double _borderWidth = 1;

    if (_isLoading) {
      return AwesomeLoader(
        loaderType: AwesomeLoader.AwesomeLoader4,
        color: Colors.pink,
      );
    }

    if (widget._agreement.movedToBlockchain ==
            false || //TODO: Ronan thinks Kevin should change this (Maybe to a null check?)
        widget._agreement.blockchainId == BigInt.from(-1)) {
      //TODO: The second condition will always fail, an exception will be thrown since Bigints are always positive
      //Show SEAL button:
      return TextButton(
        style: TextButton.styleFrom(
          padding: EdgeInsets.all(5),
          side: BorderSide(
            color: Colors.pink,
            width: _borderWidth,
          ),
        ),
        onPressed: _disabled
            ? () {
                // print('Already Pressed');
              }
            : () async {
                // print('Press Trigger');
                showLoaderDialog(context);
                try {
                  print('Calling metamask');
                  await widget._negotiationService
                      .sealAgreement(widget._agreement);
                  AgreementState as;
                  bool valid = false;
                  while (!valid) {
                    // print('in while');
                    await Future.delayed(Duration(seconds: 2));
                    widget._agreement = await widget._commonService
                        .getAgreement(widget._agreement.contractId);
                    print('ID: ' + widget._agreement.blockchainId.toString());
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
                  Navigator.of(context).pop();
                  setState(() {});
                }
              },
        child: Padding(
          padding: const EdgeInsets.all(5),
          child: Text(
            'Seal Agreement',
          ),
        ),
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
          return Text('Oops - Something went wrong');
          // does not persist
        }
        AgreementState currentState = _loadedBCAgreement.getAgreementState();

        if (currentState == AgreementState.PROPOSED) {
          //Check if payment needs to be made first
          print('SHOULD PAY: ' + _loadedBCAgreement.shouldPay().toString());
          if (_loadedBCAgreement.shouldPay()) {
            return _loadedBCAgreement.amIPaying()
                ? TextButton(
                    style: TextButton.styleFrom(
                      side: BorderSide(color: Colors.pink, width: _borderWidth),
                    ),
                    onPressed: () async {
                      showLoaderDialog(context);
                      try {
                        await widget._unisonService
                            .payAgreementMoney(widget._agreement.blockchainId);
                        Navigator.of(context).pop();
                        setState(() {});
                      } catch (error) {
                        Navigator.of(context).pop();
                        setState(() {});
                      }
                    },
                    child: Padding(
                      padding: const EdgeInsets.all(5),
                      child:
                          Text('Fulfill Payment\n(Make sure that the funds are '
                              'already in you wallet!)'),
                    ),
                  )
                : Text('The other party needs to make their promised payment');
          }

          //Check if the current user needs to Accept move to Blockchain:
          if (_loadedBCAgreement.shouldAccept()) {
            return TextButton(
              style: TextButton.styleFrom(
                side: BorderSide(color: Colors.pink, width: _borderWidth),
                padding: EdgeInsets.all(5),
              ),
              onPressed: () async {
                showLoaderDialog(context);
                try {
                  print('Accept Blockchain Agreement');
                  await widget._unisonService.acceptAgreement(
                    widget._agreement,
                  );
                  Navigator.of(context).pop();
                  setState(() {});
                } catch (error) {
                  Navigator.of(context).pop();
                  setState(() {});
                }
              },
              child: Padding(
                padding: const EdgeInsets.all(5),
                child: Text('Accept Blockchain Agreement'),
              ),
            );
          }
          return Text('Awaiting acceptance from other party');
        }
        //CurrentState >= ACCEPTED:
        if (currentState == AgreementState.ACCEPTED) {
          //Pay Platform Fee?
          return TextButton(
            style: TextButton.styleFrom(
              side: BorderSide(color: Colors.pink, width: _borderWidth),
            ),
            onPressed: () async {
              showLoaderDialog(context);
              try {
                print('Pay Platform Fee');
                await widget._unisonService.payPlatformFee(
                  widget._agreement.blockchainId,
                );
                Navigator.of(context).pop();
                setState(() {});
              } catch (error) {
                Navigator.of(context).pop();
                setState(() {});
              }
            },
            child: Padding(
              padding: const EdgeInsets.all(5),
              child: Text('Pay Platform Fee'),
            ),
          );
        }
        if (currentState == AgreementState.ACTIVE) {
          //Check Duration of BCAgreement:
          var expiryDate = DateTime.fromMillisecondsSinceEpoch(
              _loadedBCAgreement.resTime.toInt() * 1000);
          if (expiryDate.compareTo(DateTime.now()) < 0) {
            if (_loadedBCAgreement.getResolutionVote() == PartyVote.NONE) {
              return Row(
                mainAxisAlignment: MainAxisAlignment.spaceAround,
                mainAxisSize: MainAxisSize.min,
                children: [
                  TextButton(
                    style: TextButton.styleFrom(
                      side: BorderSide(color: Colors.pink, width: _borderWidth),
                    ),
                    onPressed: () async {
                      showLoaderDialog(context);
                      try {
                        print('Conclude Agreement');
                        await widget._unisonService.agreementFulfilled(
                          widget._agreement,
                          true,
                        );
                        Navigator.of(context).pop();
                        setState(() {});
                      } catch (error) {
                        Navigator.of(context).pop();
                        setState(() {});
                      }
                    },
                    child: Padding(
                      padding: const EdgeInsets.all(5),
                      child: Text('Conclude Agreement'),
                    ),
                  ),
                  SizedBox(
                    width: 20,
                  ),
                  TextButton(
                    style: TextButton.styleFrom(
                      side: BorderSide(color: Colors.pink, width: _borderWidth),
                    ),
                    onPressed: () async {
                      showLoaderDialog(context);
                      try {
                        print('Dispute Agreement');
                        await widget._unisonService.agreementFulfilled(
                          widget._agreement,
                          false,
                        );
                        Navigator.of(context).pop();
                        setState(() {});
                      } catch (error) {
                        Navigator.of(context).pop();
                        setState(() {});
                      }
                    },
                    child: Padding(
                      padding: const EdgeInsets.all(5),
                      child: Text('Dispute Agreement'),
                    ),
                  ),
                ],
              );
            }
            if (_loadedBCAgreement.getResolutionVote() == PartyVote.YES) {
              return Text('You have voted conclude');
            }
            if (_loadedBCAgreement.getResolutionVote() == PartyVote.NO) {
              return Text('You have voted dispute');
            }
          } else {
            var difference = expiryDate.difference(DateTime.now());
            Duration duration = Duration(seconds: difference.inSeconds);
            String sDuration =
                "Agreement Deadline is in ${duration.inHours} Hours "
                "and "
                "${duration.inMinutes.remainder(60)} Minutes"; //:${(duration.inSeconds
            // .remainder(60))}
            // for later
            return Text('Concludes in ' + sDuration);
          }
          //if expired/past due
          //show Resolution Options
          //voted or not

        }

        if (currentState == AgreementState.CLOSED) {
          return Text('Agreement Concluded');
        }
        print(currentState.toString());

        if (currentState == AgreementState.CONTESTED) {
          _judgeService = JudgeService();
          return FutureBuilder(
            future: _judgeService.getJury(widget._agreement.blockchainId),
            builder: (context, jurySnapshot) {
              if (jurySnapshot.connectionState == ConnectionState.done) {
                var juryConclusion = DateTime.fromMillisecondsSinceEpoch(
                    jurySnapshot.data.deadline.toInt() * 1000);
                var timeDifference = juryConclusion.difference(DateTime.now());
                Duration duration = Duration(seconds: timeDifference.inSeconds);
                String sJuryConclusion =
                    "${duration.inHours} Hours and ${duration.inMinutes.remainder(60)} Minutes";
                return Text('Jury voting concludes in: ' + sJuryConclusion);
              }
              return AwesomeLoader(
                loaderType: AwesomeLoader.AwesomeLoader4,
                color: Colors.teal,
              );
            },
          );
        }

        return Text(currentState.toString());
      },
    );
  }
}
