import 'dart:js';
import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:frontend_scv/models/contract.dart';
import 'package:frontend_scv/models/functions.dart';

class ContractDetailScreen extends StatefulWidget {

  static const routeName = '/contract-detail';
  final Contract contract;

  ContractDetailScreen(this.contract);

  @override
  _ContractDetailScreenState createState() => _ContractDetailScreenState();
}

class _ContractDetailScreenState extends State<ContractDetailScreen> {
  final condTextController = TextEditingController();
  final agrIDController = TextEditingController();

  Widget buildSectionTitle(BuildContext context, String text) {
    return Container(
      margin: EdgeInsets.symmetric(vertical: 10),
      child: Text(
        text,
        style: TextStyle(
          color: Theme.of(context).accentColor,
          fontSize: 40,
        ),
      ),
    );
  }

  Widget buildContainer(Widget child) {
    return Container(
      margin: EdgeInsets.symmetric(horizontal: 500),
      decoration: BoxDecoration(
        // color: Colors.teal,
        border: Border.all(
          color: Colors.cyan,
        ),
        borderRadius: BorderRadius.circular(10),
      ),
      padding: EdgeInsets.all(10),
      height: 400,
      child: child,
    );
  }

  Widget buildOverviewContainer(Widget child) {
    return Container(
      margin: EdgeInsets.symmetric(horizontal: 500),
      decoration: BoxDecoration(
        // color: Colors.teal,
        border: Border.all(
          color: Colors.cyan,
        ),
        borderRadius: BorderRadius.circular(10),
      ),
      padding: EdgeInsets.all(10),
      height: 200,
      child: child,
    );
  }

  void addCond(String id) async {
    String des = condTextController.text;
    String res;
    String rid = agrIDController.text;
    print("Rid: " + rid);

    try
    {
          res = await proposeCondition(rid, des);
          //Res is the conditionId
    }
    on Exception catch (e)
    {
      print ("Errord: "+e.toString()); //Should throw
      showNotify(this.context, "Error: "+e.toString());
      return;
    }
    //Term t = Term(res, res, des, TermStatus.Pending, address);
    showNotify(this.context, "Condition ID: " + res);

  }

  void acceptCond() async {
    String id = condTextController.text;
    String res;

    try
    {
      res = await acceptCondition(id);
      //Res is the status
    } on Exception catch (e)
    {
      print ("Error: "+e.toString());
      showNotify(this.context, "Error: "+e.toString());
      return;
    }
    showNotify(this.context, "Status: " + res);
  }

  void rejectCond() async {
    String id = condTextController.text;
    String res;

    try
    {
      res = await acceptCondition(id);
      //Res is the status
    } on Exception catch (e)
    {
      print ("Error: "+e.toString());
      showNotify(this.context, "Error: "+e.toString());
      return;
    }
    showNotify(this.context, "Status: " + res);
  }

  void getConditionsForAgr() async {
    final conAdd = agrIDController.text;

    List<dynamic> res;
    try
    {
      res = await getConditions(conAdd);
      //Res is the status
    } on Exception catch (e)
    {
      print ("Error: "+e.toString());
      showNotify(this.context, "Error: "+e.toString());
      return;
    }

    String resList = "";
    for (int i =0;i<res.length;i++)
      resList += res[i] + '\n';

    showNotify(this.context, "Condition IDs:\n " + resList);

  }

  @override
  Widget build(BuildContext context) {
    final selectedContract = widget.contract;
    return Scaffold(
      appBar: AppBar(
        title: Text(
          'Agreement: ${selectedContract.id}',
        ),
      ),
      floatingActionButtonLocation: FloatingActionButtonLocation.centerFloat,
      floatingActionButton: Container(
        margin: EdgeInsets.fromLTRB(750, 0, 0, 100),
        child: FloatingActionButton.extended(
          icon: Icon(Icons.add), // Web
          onPressed: () => addCond(selectedContract.id),
          label: const Text('Add New Term'),
        ),
      ),
      body: SingleChildScrollView(
        child: Column(
          children: [
            SizedBox(
              height: 20,
            ),
            buildSectionTitle(context, 'Overview'),
            buildOverviewContainer(
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceAround,
                children: [
                  Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      SizedBox(
                        height: 10,
                      ),
                      Text(
                        'Party A (Agreement creator):\n  - '
                        '${selectedContract.partyA}',
                      ),
                      SizedBox(
                        height: 10,
                      ),
                      Text(
                        'Party B:\n  - '
                        '${selectedContract.partyB}',
                      ),
                      SizedBox(
                        height: 10,
                      ),
                      Text(
                        'Creation date: ${selectedContract.createdDate}',
                      ),
                    ],
                  ),
                  Column(
                    children: [
                      SizedBox(
                        height: 10,
                      ),
                      selectedContract.movedToBlockchain
                          ? Column(
                              children: <Widget>[
                                Container(
                                  height: 100,
                                  child: Image.asset(
                                    'assets/images/onBlockchainDino.png',
                                    fit: BoxFit.cover,
                                    color: Theme.of(context).accentColor,
                                  ),
                                ),
                                Text(
                                  'Agreement on Blockchain',
                                  style: Theme.of(context).textTheme.body1,
                                ),
                              ],
                            )
                          : Column(
                              children: <Widget>[
                                Container(
                                  height: 100,
                                  child: Image.asset(
                                    'assets/images/notOnBlockchainDino.png',
                                    fit: BoxFit.cover,
                                    color: Theme.of(context).primaryColor,
                                  ),
                                ),
                                Text(
                                  'Agreement not on Blockchain',
                                  style: Theme.of(context).textTheme.body1,
                                ),
                              ],
                            ),
                    ],
                  ),
                ],
              ),
            ),
            SizedBox(
              height: 20,
            ),
            buildSectionTitle(context, 'Terms'),
            buildContainer(
              ListView.builder(
                itemBuilder: (ctx, index) => Card(
                  color: Colors.black38,
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    children: <Widget>[
                      ListTile(
                        // leading: Icon(
                        //   Icons.work,
                        //   color: Theme.of(context).accentColor,
                        // ),
                        title: Text(
                          selectedContract.terms[index].text,
                          style: TextStyle(
                            color: Colors.white70,
                            fontSize: 20,
                          ),
                        ),
                        subtitle: Text(
                          selectedContract.terms[index].description,
                          style: TextStyle(
                            color: Theme.of(context).primaryColor,
                            // fontSize: 20,
                          ),
                        ),
                      ),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.end,
                        children: <Widget>[
                          TextButton.icon(
                            icon: Icon(
                              Icons.thumb_up_alt,
                              color: Colors.green,
                            ),
                            onPressed: acceptCond,//() => {print('accept term ${index}')},
                            // selectContract(context, widget.contract),
                            // onTap: () => selectMeal(context)
                            label: Text(
                              'Accept',
                              style: TextStyle(
                                color: Colors.green,
                              ),
                            ),
                          ),
                          const SizedBox(
                            width: 10,
                          ),
                          TextButton.icon(
                            icon: Icon(
                              Icons.thumb_down_alt,
                              color: Colors.red,
                            ),
                            onPressed: rejectCond,// () => {print('reject term ${index}')},
                            // selectContract(context, widget.contract),
                            // onTap: () => selectMeal(context)
                            label: Text(
                              'Reject',
                              style: TextStyle(
                                color: Colors.red,
                              ),
                            ),
                          ),
                          const SizedBox(
                            width: 10,
                          ),
                        ],
                      ),
                      const SizedBox(
                        height: 10,
                      ),
                    ],
                  ),
                ),
                itemCount: selectedContract.terms.length,
              ),
            ),
            Column(
            children: [
              TextField(
              decoration: InputDecoration(labelText: 'Condition Text'),
              controller: condTextController,
              keyboardType: TextInputType.text,

             ),
             TextField(
             decoration: InputDecoration(labelText: 'Agreement ID'),
             controller: agrIDController,
             keyboardType: TextInputType.text,
              ),
              OutlinedButton(onPressed: getConditionsForAgr, child: Text("Get all conditions for a contract")),
            ],
           ),
          ],
        ),
      ),

      // floatingActionButton: FloatingActionButton(
      //   child: Icon(
      //     isFavorite(mealId) ? Icons.favorite : Icons.favorite_border,
      //   ),
      //   onPressed: () {
      //     // Navigator.of(context).pop(mealId);
      //     toggleFavorite(mealId);
      //   },
      // ),
    );
  }
}
