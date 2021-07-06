import 'package:flutter/material.dart';
import '../providers/contracts.dart';
import 'package:provider/provider.dart';

class ContractDetailScreen extends StatelessWidget {
  // final String title;
  //
  // ProductDetailScreen(this.title);
  //Instead of getting data like this^^^
  //
  static const routeName = '/product-detail';

  @override
  Widget build(BuildContext context) {
    //Rather retrieve arguments from routing action:
    //Extract arguments as follows:
    final productId = ModalRoute.of(context).settings.arguments as String;
    //This is the id we
    // passed from the product item
    //...
    final loadedProduct =
        //if listen is set to false - it is not an active listener (use when you
        // want dsata one time and wanna get from global storage)
        Provider.of<Contracts>(context, listen: false).findById(productId);
    return Scaffold(
      appBar: AppBar(
        title: Text(loadedProduct.title),
      ),
      body: SingleChildScrollView(
        child: Column(
          children: <Widget>[
            Container(
              height: 300,
              width: double.infinity,
              child: Image.network(
                loadedProduct.imageUrl,
                fit: BoxFit.cover,
              ),
            ),
            SizedBox(height: 10),
            Text(
              'R ${loadedProduct.price}',
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
                loadedProduct.description,
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
