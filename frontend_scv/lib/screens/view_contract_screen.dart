import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:unison/models/functions.dart';
import '../widgets/contract_conditions_panel.dart';
import '../widgets/contract_detail_info_panel.dart';
import '../providers/contracts.dart';
import '../providers/auth.dart';

class ViewContractScreen extends StatefulWidget {
  static const routeName = '/view-contract';

  @override
  _ViewContractScreenState createState() => _ViewContractScreenState();
}

class _ViewContractScreenState extends State<ViewContractScreen> {
  final _conditionTitleController = TextEditingController();
  final _conditionDescriptionController = TextEditingController();

  Future<void> _newConditionDialog() async {
    return await showDialog(
        context: context,
        builder: (context) {
          return AlertDialog(
            content: Form(
              child: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  TextFormField(
                    validator: (value) {
                      return value.isNotEmpty ? null : "Invalid Field";
                    },
                    decoration: InputDecoration(labelText: 'Title'),
                    controller: _conditionTitleController,
                  ),
                  TextFormField(
                    decoration: InputDecoration(labelText: 'Description'),
                    maxLines: 3,
                    keyboardType: TextInputType.multiline,
                    controller: _conditionDescriptionController,
                    validator: (value) {
                      if (value.isEmpty) return 'Please enter a description.';
                      if (value.length < 8)
                        return 'Please enter at least 8 '
                            'characters for the description';
                      return null;
                    },
                  ),
                ],
              ),
            ),
            actions: <Widget>[
              TextButton(
                  child: Text('okay'),
                  onPressed: () {
                    Navigator.of(context).pop();
                  }),
            ],
          );
        });
  }

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
            // alignment: Alignment.centerLeft,
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text(
                  'Agreement Conditions:',
                  style: TextStyle(
                    fontSize: 16,
                  ),
                ),
                TextButton(
                  onPressed: () async {
                    await _newConditionDialog();
                  },
                  child: Row(
                    children: [Icon(Icons.add), Text('Add New Condition')],
                  ),
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
