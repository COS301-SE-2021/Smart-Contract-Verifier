import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:unison/models/global.dart';

import '../models/contract.dart';
import '../models/contracts.dart';

class EditContractScreen extends StatefulWidget {
  static const routeName = '/edit-contract';
  @override
  _EditContractScreenState createState() => _EditContractScreenState();
}

class _EditContractScreenState extends State<EditContractScreen> {
  //Not Necessary but here it is (for shifting focus):
  final _priceFocusNode = FocusNode();
  final _partyBIdFocusNode = FocusNode();
  final _descriptionFocusNode = FocusNode();
  final _form = GlobalKey<FormState>();
  var _editedContract = Contract(
    contractId: null,
    title: '',
    description: '',
    price: 0,
    imageUrl: '',
    partyB: '',
    partyA: Global.userAddress,
    conditions: [],
    movedToBlockchain: true,
  );

  var _isInit = true;
  var _isLoading = false;

  var _initValues = {
    'title': '',
    'description': '',
    'price': '',
    'imageUrl': '',
    'partyBId': '',
    'conditions': []
  };
  @override
  void initState() {
    super.initState();
  }

  @override
  void didChangeDependencies() {
    if (_isInit) {
      final productId = ModalRoute.of(context).settings.arguments as String;
      if (productId != null) {
        _editedContract =
            Provider.of<Contracts>(context, listen: false).findById(productId);
        _initValues = {
          'title': _editedContract.title,
          'description': _editedContract.description,
          'price': _editedContract.price.toString(),
          'partyBId': _editedContract.partyB,
        };
      }
    }
    _isInit = false;
    super.didChangeDependencies();
  }

  Future<void> _saveForm() async {
    final isValid = _form.currentState.validate();
    if (!isValid) return;
    _form.currentState.save();
    setState(() {
      _isLoading = true;
    });

    if (_editedContract.contractId != null) {
      await Provider.of<Contracts>(context, listen: false)
          .updateContract(_editedContract.contractId, _editedContract);
    } else {
      try {
        await Provider.of<Contracts>(context, listen: false)
            .addContract(_editedContract);
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
    }
    setState(() {
      _isLoading = false;
    });
    Navigator.of(context).pop();
  }

  @override
  void dispose() {
    _priceFocusNode.dispose();
    _partyBIdFocusNode.dispose();
    _descriptionFocusNode.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('New Agreement'),
        actions: [
          IconButton(
            onPressed: _saveForm,
            icon: Icon(Icons.save),
          )
        ],
      ),
      body: _isLoading
          ? Center(
              child: CircularProgressIndicator(),
            )
          : Padding(
              padding: const EdgeInsets.all(16.0),
              child: Form(
                key: _form, //Global key
                child: ListView(
                  children: [
                    TextFormField(
                      initialValue: _initValues['title'],
                      decoration: InputDecoration(
                        labelText: 'Title',
                      ),
                      textInputAction: TextInputAction.next,
                      onFieldSubmitted: (_) {
                        FocusScope.of(context).requestFocus(_partyBIdFocusNode);
                      },
                      onSaved: (value) {
                        _editedContract = Contract(
                          contractId: _editedContract.contractId,
                          title: value,
                          description: _editedContract.description,
                          price: _editedContract.price,
                          imageUrl: _editedContract.imageUrl,
                          partyB: _editedContract.partyB,
                          partyA: _editedContract.partyA,
                          conditions: [],
                        );
                      },
                      validator: (value) {
                        if (value.isEmpty) return 'Please provide a value.';
                        return null;
                      },
                    ),
                    TextFormField(
                      initialValue: _initValues['partyBId'],
                      decoration: InputDecoration(
                        labelText: 'Party B Address',
                      ),
                      focusNode: _partyBIdFocusNode,
                      textInputAction: TextInputAction
                          .next, //bottom right button in soft keyboard
                      onFieldSubmitted: (_) {
                        FocusScope.of(context).requestFocus(_priceFocusNode);
                      },
                      onSaved: (value) {
                        _editedContract = Contract(
                            contractId: _editedContract.contractId,
                            title: _editedContract.title,
                            description: _editedContract.description,
                            price: _editedContract.price,
                            imageUrl: _editedContract.imageUrl,
                            partyB: value.toLowerCase(),
                            partyA: _editedContract.partyA,
                            conditions: []);
                      },
                      validator: (value) {
                        if (value.isEmpty) return 'Please provide a value.';
                        return null;
                      },
                    ),
                    TextFormField(
                      initialValue: _initValues['description'],
                      decoration: InputDecoration(labelText: 'Description'),
                      maxLines: 3,
                      keyboardType: TextInputType.multiline,
                      focusNode: _descriptionFocusNode,
                      onSaved: (value) {
                        _editedContract = Contract(
                            contractId: _editedContract.contractId,
                            title: _editedContract.title,
                            description: value,
                            price: _editedContract.price,
                            imageUrl: _editedContract.imageUrl,
                            partyB: _editedContract.partyB,
                            partyA: _editedContract.partyA,
                            conditions: []);
                      },
                      validator: (value) {
                        if (value.isEmpty) return 'Please enter a description.';
                        if (value.length < 10)
                          return 'Please enter at least 10 '
                              'characters for the description';
                        return null;
                      },
                    ),
                  ],
                ),
              ),
            ),
    );
  }
}
