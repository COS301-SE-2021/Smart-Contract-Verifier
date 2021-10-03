import 'package:awesome_loader/awesome_loader.dart';
import 'package:flutter/material.dart';
import 'package:unison/models/blockchainAgreement.dart';
import 'package:unison/models/contract.dart';
import 'package:unison/models/global.dart';
import 'package:unison/screens/evidence_screen.dart';
import 'package:unison/services/Blockchain/unisonService.dart';
import 'package:unison/services/Server/commonService.dart';
import 'package:unison/widgets/agreement/contract_condition_actions_panel.dart';
import 'package:unison/widgets/agreement/contract_conditions_panel.dart';
import 'package:unison/widgets/agreement/contract_detail_info_panel.dart';
import 'package:unison/widgets/miscellaneous/funky_text_widget.dart';

import 'messaging_screen.dart';

class ViewContractScreen extends StatefulWidget {
  static const routeName = '/view-contract';

  @override
  _ViewContractScreenState createState() => _ViewContractScreenState();
}

class _ViewContractScreenState extends State<ViewContractScreen> {
  @override
  void initState() {
    super.initState();
  }

  void _reloadView() {
    setState(() {});
  }

  BlockchainAgreement bcA = null;

  Future<BlockchainAgreement> fetchBCAgreement(agreementId) async {
    CommonService commonService = CommonService();
    UnisonService _unisonService = UnisonService();
    //await Future.delayed(Duration(milliseconds: 500));
    try {
      Contract loaded = await commonService.getAgreement(agreementId);
      if (loaded.movedToBlockchain)
        bcA = await _unisonService.getAgreement(loaded.blockchainId);
      return bcA;
    } catch (error) {
      throw (error);
    }
  }

  Future<Contract> fetchAgreement(agreementId) async {
    CommonService commonService = CommonService();
    UnisonService _unisonService = UnisonService();
    //await Future.delayed(Duration(milliseconds: 500));
    try {
      Contract loaded = await commonService.getAgreement(agreementId);
      if (loaded.movedToBlockchain)
        bcA = await _unisonService.getAgreement(loaded.blockchainId);
      return loaded;
    } catch (error) {

      throw (error);
    }
  }

  Widget build(BuildContext context) {
    final agreementId = ModalRoute.of(context).settings.arguments as String;
    String pA = '';
    String pB = '';
    return Scaffold(
      appBar: AppBar(title: FunkyText('View Agreement')),
      body: Padding(
        padding: const EdgeInsets.all(15.0),
        child: FutureBuilder(
          future: fetchAgreement(agreementId),
          builder: (context, agreementSnap) {
            if (agreementSnap.connectionState == ConnectionState.done) {
              pA = agreementSnap.data.partyA;
              pB = agreementSnap.data.partyB;
              return Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  ContractDetailInfoPanel(agreementSnap.data),
                  Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: Text(
                      'Agreement Conditions:',
                      style: TextStyle(
                        fontSize: 16,
                      ),
                    ),
                  ),
                  if (!agreementSnap.data.movedToBlockchain)
                    ContractConditionActionsPanel(
                      agreementId,
                      _reloadView,
                      pA == Global.userAddress ? pB : pA,
                    ),
                  ContractConditionsPanel(agreementSnap.data, _reloadView),
                  SizedBox(
                    height: MediaQuery.of(context).size.height * 0.1,
                  )
                ],
              );
            }
            return Center(
              child: AwesomeLoader(
                loaderType: AwesomeLoader.AwesomeLoader4,
              ),
            );
          },
        ),
      ),
      floatingActionButtonLocation: FloatingActionButtonLocation.endFloat,
      floatingActionButton: Column(
        mainAxisAlignment: MainAxisAlignment.end,
        crossAxisAlignment: CrossAxisAlignment.end,
        children: [
          FutureBuilder(
              future: fetchBCAgreement(agreementId),
              builder: (context, snapShot) {
                if (snapShot.connectionState == ConnectionState.done) {
                  if (snapShot.data == null) {
                    print('Not Contested');
                  } else {
                    if (bcA != null &&
                        bcA.getAgreementState() == AgreementState.CONTESTED)
                      return FloatingActionButton.extended(
                        onPressed: () {
                          Navigator.of(context).pushNamed(
                            EvidenceScreen.routeName,
                            arguments: {
                              'agreementId': agreementId,
                              'partyA': pA,
                              'partyB': pB,
                            },
                          );
                        },
                        label: Text('Evidence'),
                        icon: Icon(Icons.inventory_2),
                        backgroundColor: Color.fromRGBO(50, 183, 196, 1),
                      );
                  }
                }
                if (snapShot.connectionState == ConnectionState.waiting) {
                  return AwesomeLoader(
                    loaderType: AwesomeLoader.AwesomeLoader4,
                    color: Color.fromRGBO(50, 183, 196, 1),
                  );
                }
                return Container();
              }),
          SizedBox(
            height: 15,
          ),
          FloatingActionButton.extended(
            onPressed: () {
              Navigator.of(context).pushNamed(
                MessagingScreen.routeName,
                arguments: {
                  'agreementId': agreementId,
                  'partyA': pA,
                  'partyB': pB,
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