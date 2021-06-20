import 'package:flutter/material.dart';
import 'package:frontend_scv/models/contract.dart';
import 'package:frontend_scv/models/functions.dart';

class NewContract extends StatefulWidget {
  final List<Contract> _userContracts;
  NewContract(this._userContracts);

  @override
  _NewContractState createState() => _NewContractState();
}

class _NewContractState extends State<NewContract> {
  final _partyAController = TextEditingController();
  final _partyBController = TextEditingController();

  void submitData() {
    final enteredPartyA = _partyAController.text;
    final enteredPartyB = _partyBController.text;

    if (_partyAController.text.isEmpty || _partyBController.text.isEmpty) return;

    //Add agreement to list

    createInitialAgreement(enteredPartyA, enteredPartyB);

   // Contract newCon = Contract(id: id, terms: terms, status: status, partyA: partyA, partyB: partyB, createdDate: createdDate, movedToBlockchain: false, sealedDate: sealedDate, duration: duration)
    //widget._userContracts.add();

    Navigator.of(context).pop();
  }


  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.symmetric(horizontal: 500),
      child: SingleChildScrollView(
        child: Card(
          elevation: 5,
          child: Container(
            padding: EdgeInsets.only(
              top: 10,
              left: 10,
              right: 10,
              bottom: MediaQuery.of(context).viewInsets.bottom + 10, //bottom
              // tells us how much space is taken up by the soft keyboard
            ),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.end,
              children: <Widget>[
                TextField(
                  decoration: InputDecoration(labelText: 'Party A ID'),
                  controller: _partyAController,
                  //onSubmitted: submitData,
                ),
                TextField(
                  decoration: InputDecoration(labelText: 'Party B ID'),
                  controller: _partyBController,
                  keyboardType: TextInputType.number,
                 // onSubmitted:  submitData,
                ),
                Container(
                  height: 70,
                  child: Row(
                    children: [],
                  ),
                ),
                MaterialButton(
                  child: Text('Create Agreement'),
                  textColor: Theme.of(context).buttonColor,
                  color: Theme.of(context).primaryColor,
                  onPressed: submitData,
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
