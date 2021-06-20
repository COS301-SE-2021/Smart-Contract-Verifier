import 'dart:js';
import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:frontend_scv/models/contract.dart';

class ContractDetailScreen extends StatelessWidget {
  static const routeName = '/contract-detail';
  final Contract contract;

  ContractDetailScreen(this.contract);

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

  @override
  Widget build(BuildContext context) {
    final selectedContract = contract;
    return Scaffold(
      appBar: AppBar(
        title: Text(
          'Agreement: ${selectedContract.id}',
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
              //    "agreementID": "1b932958-7ce1-4816-ab34-db27f970e7c7",
              // 		"duration": null,
              // 		"partyA": "0x743Fb032c0bE976e1178d8157f911a9e825d9E23",
              // 		"partyB": "0x37Ec9a8aBFa094b24054422564e68B08aF3114B4",
              // 		"createdDate": "2021-06-19T23:47:05.454+00:00",
              // 		"sealedDate": null,
              // 		"movedToBlockchain": false,
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
                            onPressed: () => {print('accept term ${index}')},
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
                            onPressed: () => {print('reject term ${index}')},
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
