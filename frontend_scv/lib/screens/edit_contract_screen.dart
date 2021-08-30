import 'package:animated_text_kit/animated_text_kit.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:unison/models/global.dart';
import 'package:unison/widgets/funky_text_widget.dart';

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
            title: Text(
              'An error occurred!',
              style: TextStyle(color: Colors.pinkAccent),
            ),
            content: Text('Something went wrong.\nThis usually occurs when '
                'the PartyB address is not valid or active on Unison.'),
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
    Size deviceSize = MediaQuery.of(context).size;

    return Scaffold(
      appBar: AppBar(
        title: FunkyText('New Agreement'),
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
              child: Column(
                children: [
                  SizedBox(
                    height: deviceSize.height * 0.1,
                  ),
                  Card(
                    color: Color.fromRGBO(56, 61, 81, 1),
                    elevation: 15,
                    child: Padding(
                      padding: EdgeInsets.symmetric(
                          horizontal: deviceSize.width * 0.1,
                          vertical: deviceSize.width * 0.05),
                      child: Flexible(
                        child: Column(
                          children: [
                            Align(
                              alignment: Alignment.centerLeft,
                              child: SizedBox(
                                child: DefaultTextStyle(
                                  style: const TextStyle(
                                    fontSize: 14,
                                    color: Colors.pinkAccent,
                                    shadows: [
                                      Shadow(
                                        blurRadius: 7.0,
                                        color: Colors.pinkAccent,
                                        offset: Offset(0, 0),
                                      ),
                                    ],
                                  ),
                                  child: Text('New Agreement Details',
                                      style: TextStyle(fontSize: 18)),
                                ),
                                height: 20,
                              ),
                            ),
                            Form(
                              key: _form, //Global key
                              child: Column(
                                mainAxisSize: MainAxisSize.min,
                                children: [
                                  TextFormField(
                                    initialValue: _initValues['title'],
                                    decoration: InputDecoration(
                                      labelText: 'Title',
                                      // fillColor: Colors.white,

                                      focusedBorder: UnderlineInputBorder(
                                        borderSide: BorderSide(
                                          color:
                                              Color.fromRGBO(182, 80, 158, 1),
                                        ),
                                      ),
                                    ),
                                    textInputAction: TextInputAction.next,
                                    onFieldSubmitted: (_) {
                                      FocusScope.of(context)
                                          .requestFocus(_partyBIdFocusNode);
                                    },
                                    onSaved: (value) {
                                      _editedContract = Contract(
                                        contractId: _editedContract.contractId,
                                        title: value,
                                        description:
                                            _editedContract.description,
                                        price: _editedContract.price,
                                        imageUrl: _editedContract.imageUrl,
                                        partyB: _editedContract.partyB,
                                        partyA: _editedContract.partyA,
                                        conditions: [],
                                      );
                                    },
                                    validator: (value) {
                                      if (value.isEmpty)
                                        return 'Please provide a value.';
                                      return null;
                                    },
                                  ),
                                  TextFormField(
                                    initialValue: _initValues['partyBId'],
                                    decoration: InputDecoration(
                                      labelText: 'Party B Address',
                                      focusedBorder: UnderlineInputBorder(
                                        borderSide: BorderSide(
                                          color:
                                              Color.fromRGBO(182, 80, 158, 1),
                                        ),
                                      ),
                                      hintStyle: TextStyle(
                                        color: Color.fromRGBO(182, 80, 158, 1),
                                      ),
                                    ),
                                    focusNode: _partyBIdFocusNode,
                                    textInputAction: TextInputAction
                                        .next, //bottom right button in soft keyboard
                                    onFieldSubmitted: (_) {
                                      FocusScope.of(context)
                                          .requestFocus(_priceFocusNode);
                                    },
                                    onSaved: (value) {
                                      _editedContract = Contract(
                                          contractId:
                                              _editedContract.contractId,
                                          title: _editedContract.title,
                                          description:
                                              _editedContract.description,
                                          price: _editedContract.price,
                                          imageUrl: _editedContract.imageUrl,
                                          partyB: value.toLowerCase(),
                                          partyA: _editedContract.partyA,
                                          conditions: []);
                                    },
                                    validator: (value) {
                                      if (value.isEmpty)
                                        return 'Please provide a value.';
                                      return null;
                                    },
                                  ),
                                  TextFormField(
                                    initialValue: _initValues['description'],
                                    decoration: InputDecoration(
                                      labelText: 'Description',
                                      focusedBorder: UnderlineInputBorder(
                                        borderSide: BorderSide(
                                          color:
                                              Color.fromRGBO(182, 80, 158, 1),
                                        ),
                                      ),
                                    ),
                                    maxLines: 3,
                                    keyboardType: TextInputType.multiline,
                                    focusNode: _descriptionFocusNode,
                                    onSaved: (value) {
                                      _editedContract = Contract(
                                          contractId:
                                              _editedContract.contractId,
                                          title: _editedContract.title,
                                          description: value,
                                          price: _editedContract.price,
                                          imageUrl: _editedContract.imageUrl,
                                          partyB: _editedContract.partyB,
                                          partyA: _editedContract.partyA,
                                          conditions: []);
                                    },
                                    validator: (value) {
                                      if (value.isEmpty)
                                        return 'Please enter a description.';
                                      if (value.length < 10)
                                        return 'Please enter at least 10 '
                                            'characters for the description';
                                      return null;
                                    },
                                  ),
                                  SizedBox(
                                    height: 10,
                                  ),
                                ],
                              ),
                            ),
                            SizedBox(
                              height: 10,
                            ),
                            Align(
                              alignment: Alignment.centerRight,
                              child: FloatingActionButton.extended(
                                onPressed: _saveForm,
                                label: Text('Save Agreement'),
                                icon: Icon(Icons.save),
                                backgroundColor:
                                    Color.fromRGBO(182, 80, 158, 0.8),
                              ),
                            )
                          ],
                        ),
                      ),
                    ),
                  ),
                ],
              ),
            ),
    );
  }
}
