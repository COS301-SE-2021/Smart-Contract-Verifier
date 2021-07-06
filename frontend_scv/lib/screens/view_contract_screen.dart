import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../providers/contracts.dart';
import '../providers/auth.dart';

class ViewContractScreen extends StatefulWidget {
  static const routeName = '/view-contract';
  @override
  _ViewContractScreenState createState() => _ViewContractScreenState();
}

class _ViewContractScreenState extends State<ViewContractScreen> {
  @override
  Widget build(BuildContext context) {
    final contractId = ModalRoute.of(context).settings.arguments as String;
    final loadedContract =
        Provider.of<Contracts>(context, listen: false).findById(contractId);

    return Scaffold(
      appBar: AppBar(
        title: Text(loadedContract.title),
      ),
      body: SingleChildScrollView(
        child: Column(
          children: <Widget>[
            Container(
              height: 100,
              width: double.infinity,
              child: Image.network(
                loadedContract.imageUrl,
                fit: BoxFit.cover,
              ),
            ),
            SizedBox(height: 10),
            Text(
              'Test',
              style: TextStyle(
                color: Colors.grey,
                fontSize: 20,
              ),
            ),
            Text(
              'R ${loadedContract.price}',
              style: TextStyle(
                color: Colors.grey,
                fontSize: 20,
              ),
            ),
            SizedBox(
              height: 10,
            ),
            Container(
              padding: EdgeInsets.symmetric(horizontal: 10),
              width: double.infinity,
              child: Text(
                loadedContract.description,
                textAlign: TextAlign.center,
                softWrap: true,
              ),
            )
          ],
        ),
      ),
    );
  }
}
