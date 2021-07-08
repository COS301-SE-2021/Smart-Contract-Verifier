import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:unison/models/functions.dart';
import '../widgets/contract_conditions_panel.dart';
import '../widgets/contract_detail_info_panel.dart';
import '../providers/contracts.dart';
import '../providers/auth.dart';

class ViewContractScreen extends StatelessWidget {
  static const routeName = '/view-contract';

  Widget build(BuildContext context) {
    final contractId = ModalRoute.of(context).settings.arguments as String;
    final loadedContract =
        Provider.of<Contracts>(context, listen: false).findById(contractId);

    return Scaffold(
      appBar: AppBar(
        title: Text(loadedContract.title),
      ),
      body: Column(
        children: [
          ContractDetailInfoPanel(loadedContract),
          Container(
            padding: EdgeInsets.symmetric(
              vertical: 10,
              horizontal: 8,
            ),
            alignment: Alignment.centerLeft,
            child: Row(
              children: [
                Text(
                  'Agreement Conditions:',
                  style: TextStyle(
                    fontSize: 16,
                  ),
                ),
                TextButton(
                  onPressed: () {
                    print('Add Condition');
                  },
                  child: Text('Add Condition'),
                ),
              ],
            ),
          ),
          Expanded(
            child: ContractConditionsPanel(loadedContract),
            flex: 6,
          ),
          Expanded(
            flex: 1,
            child: Container(
                // TODO: More Information or actions here?
                ),
          )
        ],
      ),
      floatingActionButtonLocation: FloatingActionButtonLocation.endFloat,
      floatingActionButton: FloatingActionButton.extended(
        onPressed: () {},
        label: Text('Agreement Chat'),
        icon: Icon(Icons.chat),
      ),
    );
  }
}
