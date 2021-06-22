import 'package:flutter/material.dart';
import 'package:frontend_scv/models/contract.dart';
import 'package:frontend_scv/models/functions.dart';

class NewContract extends StatefulWidget {
  final List<Contract> _userContracts;

  NewContract(this._userContracts);

  @override
  _NewContractState createState() => _NewContractState();
}

class _NewContractState extends State<NewContract> with WidgetsBindingObserver{
  final _partyAController = TextEditingController();
  final _partyBController = TextEditingController();

 // final List<Contract> _userContracts = [];

  @override
  void initState() {
    WidgetsBinding.instance!.addObserver(this);
    super.initState();
  }

  void submitData() async {
    final enteredPartyA = _partyAController.text;
    final enteredPartyB = _partyBController.text;

    if (_partyAController.text.isEmpty || _partyBController.text.isEmpty)
      return;

    //Add agreement to list

    String id;
    try {
      print("Calling");
      id = await createInitialAgreement(enteredPartyA, enteredPartyB);
      print("ID: " +id);
       // id = "The first one";
    } on Exception catch (e) {
      print("Agreement could not be created");
      print (e.toString());
      showNotify(this.context, "Agreement could not be created \n" + e.toString());
      return;
    }

    showNotify(this.context, "Contract id: " + id);

    var now = DateTime.now();

    /*Contract newCon = Contract(
      id: id,
      terms: [],
      status: ContractStatus.Negotiation,
      partyA: enteredPartyA,
      partyB: enteredPartyB,
      createdDate: now.toString(),
      movedToBlockchain: false,
      sealedDate: "",
      duration: "",
    );*/

    Contract newCon = Contract(
      id: id,
      terms: [],
      status: ContractStatus.Negotiation,
      partyA: enteredPartyA,
      partyB: enteredPartyB,
      createdDate: now.toString(),
      movedToBlockchain: false,
      sealedDate: "",
      duration: "",
    );

    // print ("B:" + widget._userContracts.length.toString());
    //
    // print ("A: " + widget._userContracts.length.toString());
    setState(() {
      widget._userContracts.add(newCon);
    });

    Navigator.of(context).pop();
  }

  @override
  Widget build(BuildContext context) {
    void showNotify(
        String
            line) //Function to show alertDialogue to avoid some code duplication. To be used soon.
    {
      showDialog(
          context: context,
          builder: (context) {
            return AlertDialog(
              content: Text(line),
            );
          });
    }

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
