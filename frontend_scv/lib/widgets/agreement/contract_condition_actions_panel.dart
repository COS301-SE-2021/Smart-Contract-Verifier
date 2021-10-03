import 'package:date_field/date_field.dart';
import 'package:flutter/material.dart';
import 'package:grouped_buttons/grouped_buttons.dart';
import 'package:unison/models/condition.dart';
import 'package:unison/models/global.dart';
import 'package:unison/services/Server/negotiationService.dart';

class ContractConditionActionsPanel extends StatefulWidget {
  final _agreementId;
  final Function _resetMyParent;
  final String _otherParty;
  ContractConditionActionsPanel(
      this._agreementId, this._resetMyParent, this._otherParty);

  @override
  _ContractConditionActionsPanelState createState() =>
      _ContractConditionActionsPanelState();
}

enum ConditionType { Normal, Payment, Duration }

class _ContractConditionActionsPanelState
    extends State<ContractConditionActionsPanel> {
  DateTime selectedDate;

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.symmetric(
        vertical: 10,
        horizontal: 8,
      ),
      // alignment: Alignment.centerLeft,
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          TextButton(
            onPressed: () async {
              await _newConditionDialog(widget._agreementId);
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
              await _newPaymentConditionDialog(widget._agreementId);
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
              await _newDurationConditionDialog(widget._agreementId);
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
    );
  }

  final _conditionTitleController = TextEditingController();
  final _conditionDescriptionController = TextEditingController();
  final _paymentConditionAmountController = TextEditingController();
  final _durationConditionAmountController = TextEditingController();
  String _payerController = '';
  GlobalKey<FormState> _formKey = GlobalKey<FormState>();

  //Add a new duration condition
  Future<void> _newDurationConditionDialog(String contId) async {
    return await showDialog(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: Text('Add New Duration'),
          content:
              // DateTimeForm(),
              Form(
            key: _formKey,
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                DateTimeFormField(
                  decoration: const InputDecoration(
                    border: OutlineInputBorder(),
                    suffixIcon: Icon(Icons.event_note),
                    labelText: 'Expiry Date',
                  ),
                  firstDate: DateTime.now(),
                  mode: DateTimeFieldPickerMode.dateAndTime,
                  autovalidateMode: AutovalidateMode.always,
                  validator: (e) {
                    return (e?.day ?? 0) == 1
                        ? 'Please not the first day'
                        : null;
                  },
                  onDateSelected: (DateTime value) {
                    selectedDate = value;

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
                _saveForm(contId, ConditionType.Duration);
              },
            ),
          ],
        );
      },
    );
  }

  //Add a new payment condition
  Future<void> _newPaymentConditionDialog(String contId) async {
    return await showDialog(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: Text('Add New Payment'),
          content: Form(
            key: _formKey,
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                TextFormField(
                  decoration: InputDecoration(labelText: 'Enter UNT amount'),
                  keyboardType: TextInputType.number,
                  controller: _paymentConditionAmountController,
                  validator: (value) {
                    if (value.isEmpty) return 'Please enter an amount.';
                    if (!isNumeric(value)) return 'Please enter a double.';
                    return null;
                  },
                ),
                RadioButtonGroup(
                  activeColor: Color.fromRGBO(182, 80, 158, 1),
                  labels: <String>[
                    "I will be paying",
                    "The other party will pay",
                  ],
                  onSelected: (String selected) {
                    _payerController = selected;
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
                _payerController == ''
                    ? null
                    : _saveForm(contId, ConditionType.Payment);
              },
            ),
          ],
        );
      },
    );
  }

  bool isNumeric(String s) {
    if (s == null) {
      return false;
    }
    return double.tryParse(s) != null;
  }

  //Add a new normal condition
  Future<void> _newConditionDialog(String agreementId) async {
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
                    if (value.length > 128)
                      return 'Descriptions cannot be more than 128 characters'
                          '.';
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
                _saveForm(agreementId, ConditionType.Normal);
              },
            ),
          ],
        );
      },
    );
  }

  Future<void> _saveForm(String aId, ConditionType type) async {
    final isValid = _formKey.currentState.validate();
    NegotiationService negotiationService = NegotiationService();

    Condition newCondition = Condition(
      title: '',
      proposedBy: Global.userAddress,
      description: '',
      agreementId: aId,
    );

    if (type == ConditionType.Normal) {
      newCondition = Condition(
        title: _conditionTitleController.text,
        proposedBy: Global.userAddress,
        description: _conditionDescriptionController.text,
        agreementId: aId,
      );
    } else {}
    if (!isValid) return;
    _formKey.currentState.save();
    // setState(() {
    //   // _isLoading = true;
    // });

    try {
      //Save to DB:
      if (type == ConditionType.Payment) {
        await negotiationService.setPayment(
          aId,
          _payerController == "I will be paying"
              ? Global.userAddress
              : widget._otherParty,
          double.parse(_paymentConditionAmountController.text),
        );
        _payerController = '';
      } else if (type == ConditionType.Duration) {
        await negotiationService.setDuration(
          aId,
          selectedDate,
        );
      } else {
        await negotiationService.saveCondition(newCondition);
      }
      widget._resetMyParent();
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
    Navigator.of(context).pop();
  }
}
