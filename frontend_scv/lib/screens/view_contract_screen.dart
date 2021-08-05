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

  GlobalKey<FormState> _formKey = GlobalKey<FormState>();

  Future<void> _newConditionDialog() async {
    return await showDialog(
        context: context,
        builder: (context) {
          return AlertDialog(
            title: Text('Add New Condition'),
            content: Form(
              key: _formKey,
              child: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  TextFormField(
                    validator: (value) {
                      return value.isNotEmpty ? null : "Please enter a title.";
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
                            'characters for the description.';
                      return null;
                    },
                  ),
                ],
              ),
            ),
            actions: <Widget>[
              TextButton(
                child: Text('Discard'),
                onPressed: () {
                  Navigator.of(context).pop();
                },
              ),
              TextButton(
                child: Text('Add'),
                onPressed: () {
                  //Check Form State
                  if (_formKey.currentState.validate()) {
                    //if true - i.e the form is valid
                    Navigator.of(context).pop();
                  }
                  print('Add new condition: ${_conditionTitleController.text}'
                      '** ** **${_conditionDescriptionController.text}');
                  //TODO: Add New Condition (Server Call) here:
                },
              ),
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
