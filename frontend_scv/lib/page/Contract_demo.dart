import 'dart:async';
import 'dart:math';

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:http/http.dart'; // as http;
import 'package:web3dart/web3dart.dart';

class ContractDemo extends StatefulWidget {
  @override
  _DemoState createState() => _DemoState();
}

class _DemoState extends State<ContractDemo> {
  double horizontalPadding = 20;
  double verticalPadding = 15;

  _DemoState() {
    if (kIsWeb) {
      horizontalPadding = 500;
      verticalPadding = 80;
    }
  }

  String address = '';

  // "0x99D00540396bb0cB815dF0702d0Ec4Ab507f6891"; //In the more final version, this will be provided by the user
  String address2 = '';

  //"0xc1022300D87929b99A801f75FF194de5D38DDcCb"; //In the more final version, this will be provided by the user
  String contractAddress = '';

  //"0x096Dc0abbBb79F7669B8bFa6f7dd01EaEeDD0519"; //Address of deployed contract
// This private key is for a test account, it is not of any value
  String pk = '';

  //"5fcf2d56b9173c04cf90afc671faef0f5466fe01d6ddd61c158a3961d45ad10a"; //Private key of test account

  String apiResult1 = ""; //These are part of the demo
  String apiResult2 = "";
  String apiResult3 = "";
  String apiResult4 = "";

  int tryVal = -1;
  BigInt sendVal = BigInt.from(0);

  Web3Client bcClient =
      Web3Client("HTTP://127.0.0.1:8545", Client()); //BlockChainClient

  final conValInputController =
      TextEditingController(); //Contract value input controller
  final pkInputController =
      TextEditingController(); //Private key value input controller
  final address2InputController =
      TextEditingController(); //Second address value input controller
  final contractAddressInputController =
      TextEditingController(); //Input the contract address

  String nicerJSON(
      String
          j) //Just formats a JSON object using very crude methods for improved readability. I'm proud of it so I'm keeping it here
  {
    int buffer = 0;
    String res = '';
    for (int i = 0; i < j.length; i++) {
      if (j[i] == '{' || j[i] == '[' || j[i] == ',') {
        if (j[i] != ',') {
          res += '\n';
          for (int k = 0; k < buffer; k++) res += ' ';
          buffer += 2;
        }
        res += j[i] + '\n';
        for (int k = 0; k < buffer; k++) res += ' ';
      } else if (j[i] == '}' || j[i] == ']') {
        res += '\n';
        buffer = max(0, buffer - 2);
        for (int k = 0; k < buffer; k++) res += ' ';
        res += j[i];
      } else if (j[i] == ':')
        res += ' ' + j[i] + ' ';
      else
        res += j[i];
    }
    return res;
  }

  void loadValues() {
    pk = pkInputController.text;
    contractAddress = contractAddressInputController.text;
    address2 = address2InputController.text;
    sendVal = BigInt.parse(conValInputController.text);
  }

  Future<DeployedContract> getContract() async {
    String abi = await rootBundle.loadString("Assets/abi.json");
    final theContract = DeployedContract(ContractAbi.fromJson(abi, "SCV"),
        EthereumAddress.fromHex(contractAddress)); //Contract from ID
    return theContract;
  }

  Future<List<dynamic>> makeReadCall(
      String function, List<dynamic> args) async {
    final theContract = await getContract();
    final fun = theContract.function(function);
    List<dynamic> theResult =
        await bcClient.call(contract: theContract, function: fun, params: args);
    return theResult;
  }

  Future<String> makeWriteCall(String funct, List<dynamic> args) async {
    EthPrivateKey cred =
        EthPrivateKey.fromHex(pk); //Credentials from private key
    final theContract = await getContract();
    final fun = theContract.function(funct);
    final theResult = await bcClient.sendTransaction(
        cred,
        Transaction.callContract(
            contract: theContract, function: fun, parameters: args));
    return theResult;
  }

  void openAgreement() async {
    try {
      loadValues();
    } on Exception catch (exception) {
      //Failure to parse
      showDialog(
          context: context,
          builder: (context) {
            return AlertDialog(
              content: Text(exception.toString()),
            );
          });

      return;
    }
    String theResult = await makeWriteCall(
        "createAgreement", [EthereumAddress.fromHex(address2), sendVal]);
    setState(() {
      apiResult3 = theResult;
    });
  }

  void acceptAgreement() async {
    try {
      loadValues();
    } on Exception catch (exception) {
      //Failure to parse
      showDialog(
          context: context,
          builder: (context) {
            return AlertDialog(
              content: Text(exception.toString()),
            );
          });
      return;
    }
    String theResult = await makeWriteCall("acceptAgreement", [sendVal]);
    setState(() {
      apiResult1 = theResult;
    });
  }

  void closeAgreement() async {
    try {
      loadValues();
    } on Exception catch (exception) {
      //Failure to parse
      showDialog(
          context: context,
          builder: (context) {
            return AlertDialog(
              content: Text(exception.toString()),
            );
          });
      return;
    }
    String theResult = await makeWriteCall("closeAgreement", [sendVal]);
    setState(() {
      apiResult2 = theResult;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: EdgeInsets.symmetric(
        vertical: verticalPadding,
        horizontal: horizontalPadding,
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.center,
        children: <Widget>[
          TextField(
            style: TextStyle(color: Colors.white70),
            controller: pkInputController,
            decoration: InputDecoration(
              labelText: 'Private Key',
            ),
          ),
          TextField(
            style: TextStyle(color: Colors.white70),
            controller: address2InputController,
            decoration: InputDecoration(labelText: "Second Address"),
          ),
          TextField(
            style: TextStyle(
              color: Colors.white70,
            ),
            controller: contractAddressInputController,
            decoration: InputDecoration(
              labelText: "Contract Address",
            ),
          ),
          TextField(
              style: TextStyle(color: Colors.white70),
              controller: conValInputController,
              keyboardType: TextInputType.number,
              inputFormatters: <TextInputFormatter>[
                FilteringTextInputFormatter.digitsOnly,
              ],
              decoration: InputDecoration(labelText: "Processing Value")),
          Container(
            margin: EdgeInsets.fromLTRB(0, 20, 0, 0),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: [
                OutlinedButton(
                  onPressed: getContract,
                  child: Text(
                    "Get\nContract",
                    textAlign: TextAlign.center,
                  ),
                ),
                OutlinedButton(
                  onPressed: openAgreement,
                  child: Text(
                    "Open\nContract",
                    textAlign: TextAlign.center,
                  ),
                ),
                OutlinedButton(
                  onPressed: acceptAgreement,
                  child: Text(
                    "Accept\nContract",
                    textAlign: TextAlign.center,
                  ),
                ),
                OutlinedButton(
                  onPressed: closeAgreement,
                  child: Text(
                    "Close\nContract",
                    textAlign: TextAlign.center,
                  ),
                ),
              ],
            ),
          ),
          Row(
            children: [
              Container(
                margin: EdgeInsets.symmetric(vertical: 30, horizontal: 0),
                child: Column(
                  children: [
                    Text(
                      "Open Contract Return Value:\n$apiResult3\n",
                      style: TextStyle(
                        color: Colors.blue,
                      ),
                    ),
                    Text(
                      "Accept Contract Return Value:\n$apiResult1\n",
                      style: TextStyle(color: Colors.green),
                    ),
                    Text(
                      "Close Contract Return Value:\n$apiResult2\n",
                      style: TextStyle(color: Colors.orange),
                    ),
                    Text(
                      "Get Contract Return Value:\n\n", //TODO
                      style: TextStyle(
                        color: Colors.amber,
                      ),
                    ),
                  ],
                ),
              )
            ],
          )
        ],
      ),
    );
  }
}
