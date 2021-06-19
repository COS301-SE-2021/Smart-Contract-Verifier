import 'package:flutter/material.dart';

class NewContract extends StatefulWidget {
  final Function addCont;

  NewContract(this.addCont);

  @override
  _NewContractState createState() => _NewContractState();
}

class _NewContractState extends State<NewContract> {
  final _titleController = TextEditingController();
  final _partyAController = TextEditingController();
  final _partyBController = TextEditingController();

  _NewContractState() {
    print('constructor');
  }

  @override
  void initState() {
    print('initState');
    //Make http request here (fetching initial data)
    super.initState();
  }

  @override
  void didUpdateWidget(NewContract oldWidget) {
    //re-fetch data
    print('didUpdate widget');
    super.didUpdateWidget(oldWidget);
  }

  @override
  void dispose() {
    //cleaning up data/connection
    print('dispose');
    super.dispose();
  }

  void _submitData() {
    final enteredTitle = _titleController.text;
    // final enteredAmount = double.parse(_amountController.text);
    //TODO
    // if (_amountController.text.isEmpty) return;

    // if (enteredTitle.isEmpty || enteredAmount <= 0 || _selectedDate == null)
    //   return;
    // widget.addTx(
    //   enteredTitle,
    //   enteredAmount,
    //   _selectedDate,
    // );
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
                  onSubmitted: (_) => _submitData(),
                ),
                TextField(
                  decoration: InputDecoration(labelText: 'Party B ID'),
                  controller: _partyBController,
                  keyboardType: TextInputType.number,
                  onSubmitted: (_) => _submitData(),
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
                  onPressed: _submitData,
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
