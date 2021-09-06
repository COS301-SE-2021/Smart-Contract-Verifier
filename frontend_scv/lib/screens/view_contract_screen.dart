import 'package:awesome_loader/awesome_loader.dart';
import 'package:flutter/material.dart';
import 'package:unison/models/contract.dart';
import 'package:unison/services/Server/commonService.dart';
import 'package:unison/widgets/contract_condition_actions_panel.dart';
import 'package:unison/widgets/contract_conditions_panel.dart';
import 'package:unison/widgets/contract_detail_info_panel.dart';
import 'package:unison/widgets/funky_text_widget.dart';

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

  Future<Contract> fetchAgreement(agreementId) async {
    CommonService commonService = CommonService();
    await Future.delayed(Duration(milliseconds: 500));
    try {
// <<<<<<< dev_front_improvements_api
      //Save to DB:
//       if (type == ConditionType.Payment) {
//         await negotiationService.setPayment(
//           cId,
//           Global.userAddress, //TODO: change this to Party Input
//           double.parse(_paymentConditionAmountController.text),
//         );
//       } else if (type == ConditionType.Duration) {
//         await negotiationService.setDuration(
//           cId,
//           (60 * 60 * double.parse(_durationConditionAmountController.text)),
//         ); //Updated to ask for hours, send through in seconds
//       } else {
//         await negotiationService.saveCondition(newCondition);
//       }
// =======
      Contract loaded = await commonService.getAgreement(agreementId);
      return loaded;
// >>>>>>> dev_front_improvements
    } catch (error) {
      print(error);
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
                      ContractConditionActionsPanel(agreementId, _reloadView),
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
          )),
      floatingActionButtonLocation: FloatingActionButtonLocation.endFloat,
      floatingActionButton: FloatingActionButton.extended(
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
    );
  }
}
