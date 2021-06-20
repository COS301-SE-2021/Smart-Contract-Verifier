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
        style: Theme.of(context).textTheme.title,
      ),
    );
  }

  Widget buildContainer(Widget child) {
    return Container(
      decoration: BoxDecoration(
        color: Colors.white,
        border: Border.all(
          color: Colors.grey,
        ),
        borderRadius: BorderRadius.circular(10),
      ),
      margin: EdgeInsets.all(10),
      padding: EdgeInsets.all(10),
      height: 150,
      width: 300,
      child: child,
    );
  }

  @override
  Widget build(BuildContext context) {
    // final mealId = ModalRoute.of(context)!.settings.arguments as String;

    final selectedContract = contract;
    //firstWhere returns 1 element

    return Scaffold(
      appBar: AppBar(
        title: Text(
          'test',
        ),
      ),
      body: SingleChildScrollView(
        child: Column(
          children: [
            buildSectionTitle(context, 'Terms'),
            buildContainer(
              ListView.builder(
                itemBuilder: (ctx, index) => Card(
                  color: Theme.of(context).accentColor,
                  child: Padding(
                    padding: const EdgeInsets.symmetric(
                      vertical: 5,
                      horizontal: 10,
                    ),
                    child: Text("selectedContract.terms[index].description"),
                  ),
                ),
                itemCount: selectedContract.terms.length,
              ),
            ),
            buildSectionTitle(context, 'Steps'),
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
