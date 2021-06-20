import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:frontend_scv/model/call_smart_contract.dart';

class DemoPage extends StatelessWidget {
  final privateKey = TextEditingController();
  final secondAddress = TextEditingController();
  final contractAddress = TextEditingController();
  final processingValue = TextEditingController();

  double horizontalPadding = 20;
  double verticalPadding = 15;

  DemoPage() {
    if (kIsWeb) {
      horizontalPadding = 500;
      verticalPadding = 80;
    }
    ;
  }

  @override
  Widget build(BuildContext context) => Scaffold(
        appBar: AppBar(
          title: Text('Demo 1'),
          centerTitle: true,
          backgroundColor: Colors.teal,
        ),
        body: Card(
          child: Container(
            padding: EdgeInsets.symmetric(
              vertical: verticalPadding,
              horizontal: horizontalPadding,
            ),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.end,
              children: [
                TextField(
                  decoration: InputDecoration(labelText: 'Private Key'),
                  controller: privateKey,
                ),
                TextField(
                  decoration: InputDecoration(labelText: 'Second Address'),
                  controller: secondAddress,
                ),
                TextField(
                  decoration: InputDecoration(labelText: 'Contract Address'),
                  controller: contractAddress,
                ),
                TextField(
                  decoration:
                      InputDecoration(labelText: 'Value for Processing'),
                  controller: processingValue,
                ),
                Container(
                  margin: EdgeInsets.symmetric(vertical: 20, horizontal: 0),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                    children: [
                      ElevatedButton(
                        onPressed: () => {
                          // SmartContractRequest(
                          //   privateKey: privateKey.text,
                          //   secondAddress: secondAddress.text,
                          //   contractAddress: contractAddress.text,
                          //   processingValue: processingValue.text,
                          // ).openAgreement()
                        }, //openAgreement,
                        child: Text(
                          'Open\nContract',
                          textAlign: TextAlign.center,
                        ),
                      ),
                      ElevatedButton(
                        onPressed: null, //,
                        child: Text(
                          'Accept\nContract',
                          textAlign: TextAlign.center,
                        ),
                      ),
                      ElevatedButton(
                        onPressed: null, //,
                        child: Text(
                          'Close\nContract',
                          textAlign: TextAlign.center,
                        ),
                      ),
                    ],
                  ),
                )

                // FlatButton(
                //   onPressed: () => {
                //     // print("title $titleInput amount$amountInput")
                //     print("private key: ${privateKey.text} \n"
                //         "second address: ${secondAddress.text}")
                //   },
                //   child: Text("Print Values"),
                // )
              ],
            ),
          ),
        ),
      );
}
