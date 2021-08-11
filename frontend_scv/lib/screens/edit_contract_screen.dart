import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../models/contract.dart';
import '../models/contracts.dart';

class EditContractScreen extends StatefulWidget {
  static const routeName = '/edit-contract';

  //USE STATEFUL for Form input
  @override
  _EditContractScreenState createState() => _EditContractScreenState();
}

class _EditContractScreenState extends State<EditContractScreen> {
  //Not Necessary but here it is (for shifting focus):
  final _priceFocusNode = FocusNode();
  final _partyBIdFocusNode = FocusNode();
  final _descriptionFocusNode = FocusNode();
  final _imageUrlFocusNode = FocusNode();
  final _imageUrlController = TextEditingController();
  //Global Key - used to interact with a widget within your code - very rare
  //Normally used for forms:
  final _form = GlobalKey<FormState>(); //essentially 'hooks' into forms state
  //use this Product and update it when saveForm is called
  var _editedContract = Contract(
    contractId: null,
    title: '',
    description: '',
    price: 0,
    imageUrl: '',
    partyBId: '',
    conditions: [],
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
    _imageUrlFocusNode.addListener(_updateImageUrl);
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
          'imageUrl': '',
          'partyBId': '',
        };
        _imageUrlController.text = _editedContract.imageUrl;
      }
    } //
    _isInit = false;
    super.didChangeDependencies();
  }

  void _updateImageUrl() {
    if (!_imageUrlFocusNode.hasFocus) {
      if ((!_imageUrlController.text.startsWith('http') &&
              !_imageUrlController.text.startsWith('https')) ||
          (!_imageUrlController.text.endsWith('.png') &&
              !_imageUrlController.text.endsWith('.jpg') &&
              !_imageUrlController.text.endsWith('.jpeg'))) {
        return;
      }
      setState(() {});
    }
  }

  Future<void> _saveForm() async {
    //validates inputs on the form -> executes the 'validator' of each input:
    //return True if no errors i.e. no strings, or False if errors
    final isValid = _form.currentState.validate();
    if (!isValid) return;
    _form.currentState.save();
    //^^^^saves the form -> executes the 'onSaved' of each input
    setState(() {
      _isLoading = true;
    });

    if (_editedContract.contractId != null) {
      //editing
      //can wrap in try catch here
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
              FlatButton(
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
    _imageUrlFocusNode.removeListener(_updateImageUrl);
    _priceFocusNode.dispose();
    _partyBIdFocusNode.dispose();
    _descriptionFocusNode.dispose();
    _imageUrlController.dispose();
    _imageUrlFocusNode.dispose();
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
                        // errorText:
                      ),
                      textInputAction: TextInputAction.next,
                      onFieldSubmitted: (_) {
                        //not neccessary
                        FocusScope.of(context).requestFocus(_partyBIdFocusNode);
                      },
                      onSaved: (value) {
                        _editedContract = Contract(
                          //because the values of a Product
                          // are final - we need to create a new Product (we cannot
                          // simply edit them) and then replace the existing one
                          contractId: _editedContract.contractId,
                          title: value, //This is the value that needs to be
                          // updated
                          description: _editedContract.description,
                          price: _editedContract.price,
                          imageUrl: _editedContract.imageUrl,
                          partyBId: _editedContract.partyBId,
                          isFavorite: _editedContract.isFavorite,
                          conditions: [],
                        );
                      },
                      validator: (value) {
                        //Used to validate the value in the edit field
                        //returning null -> input is correct
                        //returning text -> 'This  is wrong' (something is wrong)
                        // -> message for user
                        if (value.isEmpty) return 'Please provide a value.';
                        return null;
                      },
                    ),
                    TextFormField(
                      initialValue: _initValues['partyBId'],
                      decoration: InputDecoration(
                        labelText: 'Party B Address',
                        // errorText:
                      ),
                      focusNode: _partyBIdFocusNode,
                      textInputAction: TextInputAction
                          .next, //bottom right button in soft keyboard
                      onFieldSubmitted: (_) {
                        //not neccessary
                        FocusScope.of(context).requestFocus(_priceFocusNode);
                      },
                      onSaved: (value) {
                        _editedContract = Contract(
                            //because the values of a Product
                            // are final - we need to create a new Product (we cannot
                            // simply edit them) and then replace the existing one
                            contractId: _editedContract.contractId,
                            title:
                                _editedContract.title, //This is the value that
                            // needs to be
                            // updated
                            description: _editedContract.description,
                            price: _editedContract.price,
                            imageUrl: _editedContract.imageUrl,
                            partyBId: value,
                            isFavorite: _editedContract.isFavorite,
                            conditions: []);
                      },
                      validator: (value) {
                        //Used to validate the value in the edit field
                        //returning null -> input is correct
                        //returning text -> 'This  is wrong' (something is wrong)
                        // -> message for user
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
                            partyBId: _editedContract.partyBId,
                            isFavorite: _editedContract.isFavorite,
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
                    Row(
                      crossAxisAlignment: CrossAxisAlignment.end,
                      children: [
                        Container(
                          width: 100,
                          height: 100,
                          margin: EdgeInsets.only(top: 8, right: 10),
                          decoration: BoxDecoration(
                              border: Border.all(
                            color: Colors.grey,
                          )),
                          child: _imageUrlController.text.isEmpty
                              ? Center(child: Text('Enter a URL'))
                              : FittedBox(
                                  fit: BoxFit.cover,
                                  child:
                                      Image.network(_imageUrlController.text),
                                ),
                        ),
                        Expanded(
                          child: TextFormField(
                            decoration: InputDecoration(labelText: 'Image URL'),
                            keyboardType: TextInputType.url,
                            textInputAction: TextInputAction.done,
                            controller: _imageUrlController,
                            focusNode: _imageUrlFocusNode,
                            onFieldSubmitted: (_) {
                              _saveForm();
                            },
                            onSaved: (value) {
                              _editedContract = Contract(
                                  contractId: _editedContract.contractId,
                                  title: _editedContract.title,
                                  description: _editedContract.description,
                                  price: _editedContract.price,
                                  imageUrl: value,
                                  partyBId: _editedContract.partyBId,
                                  isFavorite: _editedContract.isFavorite,
                                  conditions: []);
                            },
                            validator: (value) {
                              if (value.isEmpty) {
                                return 'Please enter an image URL.';
                              }
                              if (!value.startsWith('http') &&
                                  !value.startsWith('https')) {
                                return 'Please enter a valid URL.';
                              }
                              if (!value.endsWith('.png') &&
                                  !value.endsWith('.jpg') &&
                                  !value.endsWith('.jpeg')) {
                                return 'Please enter a valid image URL.';
                              }
                              return null;
                            },
                          ),
                        )
                      ],
                    ),
                  ],
                ),
              ),
            ),
    );
  }
}
