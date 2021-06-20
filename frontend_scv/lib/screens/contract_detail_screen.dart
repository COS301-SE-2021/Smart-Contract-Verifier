import 'dart:js';

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
            // buildSectionTitle(context, 'Steps'),
            // buildContainer(
            //   ListView.builder(
            //     itemBuilder: (ctx, index) => Column(
            //       children: [
            //         ListTile(
            //           leading: CircleAvatar(
            //             child: Text('# ${index + 1}'),
            //           ),
            //           title: Text(selectedMeal.steps[index]),
            //         ),
            //         Divider(),
            //       ],
            //     ),
            //     itemCount: selectedMeal.steps.length,
            //   ),
            // ),

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
