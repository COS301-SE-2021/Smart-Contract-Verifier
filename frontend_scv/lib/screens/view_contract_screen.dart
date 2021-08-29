import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:unison/models/condition.dart';
import 'package:unison/models/global.dart';
import 'package:unison/screens/messaging_screen.dart';
import 'package:unison/services/Server/negotiationService.dart';

import '../models/contracts.dart';
import '../widgets/contract_conditions_panel.dart';
import '../widgets/contract_detail_info_panel.dart';

class ViewContractScreen extends StatefulWidget {
  static const routeName = '/view-contract';

  @override
  _ViewContractScreenState createState() => _ViewContractScreenState();
}

enum ConditionType { Normal, Payment, Duration }

class _ViewContractScreenState extends State<ViewContractScreen> {
  final _conditionTitleController = TextEditingController();
  final _conditionDescriptionController = TextEditingController();
  final _paymentConditionAmountController = TextEditingController();
  final _durationConditionAmountController = TextEditingController();
  var _isLoading = false;
  // var _isInit = true;

  NegotiationService negotiationService = NegotiationService();

  GlobalKey<FormState> _formKey = GlobalKey<FormState>();

  @override
  void initState() {
    // print('InitState()');
    super.initState();
  }

  Future<void> _saveForm(String cId, ConditionType type) async {
    final isValid = _formKey.currentState.validate();

    Condition newCondition = Condition(
      title: '',
      proposedBy: Global.userAddress,
      description: '',
      agreementId: cId,
    );

    if (type == ConditionType.Normal) {
      newCondition = Condition(
        title: _conditionTitleController.text,
        proposedBy: Global.userAddress,
        description: _conditionDescriptionController.text,
        agreementId: cId,
      );
    } else {}
    if (!isValid) return;
    _formKey.currentState.save();
    //^^^^saves the form -> executes the 'onSaved' of each input
    setState(() {
      _isLoading = true;
    });

    try {
      //Save to DB:
      if (type == ConditionType.Payment) {
        await negotiationService.setPayment(
          cId,
          Global.userAddress, //TODO: change this to Party Input
          double.parse(_paymentConditionAmountController.text),
        );
      } else if (type == ConditionType.Duration) {
        await negotiationService.setDuration(
          cId,
          (60 * 60 * double.parse(_durationConditionAmountController.text)),
        ); //Updated to ask for hours, send through in seconds
      } else {
        await negotiationService.saveCondition(newCondition);
      }
      print('new condition saved');
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
      Provider.of<Contracts>(context, listen: false).fetchAndSetContracts();
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
                  _saveForm(contId, ConditionType.Normal);
                },
              ),
            ],
          );
        });
  }

  bool isNumeric(String s) {
    if (s == null) {
      return false;
    }
    return double.tryParse(s) != null;
  }

  Future<void> _newSpecialConditionDialog(
      String contId, ConditionType type) async {
    return await showDialog(
        context: context,
        builder: (context) {
          return AlertDialog(
            title: type == ConditionType.Payment
                ? Text('Add New Payment '
                    'Condition')
                : Text('Add New Duration '
                    'Condition'),
            content: Form(
              key: _formKey,
              child: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  TextFormField(
                    decoration: type == ConditionType.Payment
                        ? InputDecoration(labelText: 'Enter UNT amount')
                        : InputDecoration(
                            labelText: 'Enter Duration (in hours)'
                                ' from the seal time'),
                    keyboardType: TextInputType.number,
                    controller: type == ConditionType.Payment
                        ? _paymentConditionAmountController
                        : _durationConditionAmountController,
                    validator: (value) {
                      if (value.isEmpty) return 'Please enter an amount.';
                      if (!isNumeric(value)) return 'Please enter a double.';
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
                  _paymentConditionAmountController.clear();
                  _durationConditionAmountController.clear();
                  Navigator.of(context).pop();
                },
              ),
              TextButton(
                child: Text('Add'),
                onPressed: () {
                  type == ConditionType.Payment
                      ? _saveForm(contId, ConditionType.Payment)
                      : _saveForm(contId, ConditionType.Duration);
                },
              ),
            ],
          );
        });
  }

  Widget build(BuildContext context) {
    final contractId = ModalRoute.of(context).settings.arguments as String;
    final loadedContract =
        Provider.of<Contracts>(context, listen: true).findById(contractId);

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
                TextButton(
                  onPressed: () async {
                    await _newSpecialConditionDialog(
                        contractId, ConditionType.Payment);
                  },
                  child: Row(
                    children: [
                      Icon(Icons.add),
                      Text('Add New Payment Condition'),
                    ],
                  ),
                ),
                TextButton(
                  onPressed: () async {
                    await _newSpecialConditionDialog(
                        contractId, ConditionType.Duration);
                  },
                  child: Row(
                    children: [
                      Icon(Icons.add),
                      Text('Add New Duration Condition'),
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
        onPressed: () {
          Navigator.of(context).pushNamed(
            MessagingScreen.routeName,
            arguments: loadedContract.contractId,
          );
        },
        label: Text('Agreement Chat'),
        icon: Icon(Icons.chat),
      ),
    );
  }
}
