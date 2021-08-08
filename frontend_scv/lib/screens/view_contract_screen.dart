import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:unison/providers/condition.dart';
import 'package:unison/providers/global.dart';
import 'package:unison/services/Server/negotiationService.dart';
import '../widgets/contract_conditions_panel.dart';
import '../widgets/contract_detail_info_panel.dart';
import '../providers/contracts.dart';

class ViewContractScreen extends StatefulWidget {
  static const routeName = '/view-contract';

  @override
  _ViewContractScreenState createState() => _ViewContractScreenState();
}

class _ViewContractScreenState extends State<ViewContractScreen> {
  final _conditionTitleController = TextEditingController();
  final _conditionDescriptionController = TextEditingController();
  var _isLoading = false;
  NegotiationService negotiationService = NegotiationService();

  GlobalKey<FormState> _formKey = GlobalKey<FormState>();

  Future<void> _saveForm(String cId) async {
    final isValid = _formKey.currentState.validate();

    Condition newCondition = Condition(
      title: _conditionTitleController.text,
      proposedBy: Global.userAddress,
      description: _conditionDescriptionController.text,
      agreementId: cId,
    );

    if (!isValid) return;
    _formKey.currentState.save();
    //^^^^saves the form -> executes the 'onSaved' of each input
    setState(() {
      _isLoading = true;
    });

    try {
      await negotiationService.saveCondition(newCondition);

      //TODO: Add New Condition (Server Call) here:
      // await Provider.of<Contract>(context, listen: false)
      //     .addCondition(_newCondition);
      print('Add new condition: ${_conditionTitleController.text}'
          '** ** **${_conditionDescriptionController.text}');
    } catch (error) {
      await showDialog(
        context: context,
        builder: (ctx) => AlertDialog(
          title: Text('An error occurred!'),
          content: Text('Something went wrong.'),
          actions: <Widget>[
            TextButton(
              child: Text('Okay'),
              onPressed: () {
                Navigator.of(ctx).pop();
              },
            )
          ],
        ),
      );
    }
    setState(() {
      _isLoading = false;
    });
    Navigator.of(context).pop();
  }

  Future<void> _newConditionDialog(String contId) async {
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
                      if (value.isEmpty) {
                        return 'Please enter a title.';
                      } else {
                        return null;
                      }
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
                  _conditionTitleController.clear();
                  _conditionDescriptionController.clear();
                  Navigator.of(context).pop();
                },
              ),
              TextButton(
                child: Text('Add'),
                onPressed: () {
                  _saveForm(contId);
                },
              ),
            ],
          );
        });
  }

  Widget build(BuildContext context) {
    final contractId = ModalRoute.of(context).settings.arguments as String;
    print('\n\n_______________VIEW__\n\n${contractId}\n'
        '\n_________________\n\n');

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
                    await _newConditionDialog(contractId);
                  },
                  child: Row(
                    children: [
                      Icon(Icons.add),
                      Text('Add New Condition'),
                    ],
                  ),
                ),
              ],
            ),
          ),
          Expanded(
            child: _isLoading
                ? Center(
                    child: CircularProgressIndicator(),
                  )
                : ContractConditionsPanel(loadedContract),
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
