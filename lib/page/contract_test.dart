import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'dart:math';
import 'package:http/http.dart';// as http;
import 'package:web3dart/web3dart.dart';
import 'dart:async';


class ContractDemo extends StatefulWidget {


  @override
  _DemoState createState() => _DemoState();
}

class _DemoState extends State<ContractDemo> {

  final String address = "0xE4452aB61d4F8C11870b3A996aa94885037E588F"; //In the more final version, this will be provided by the user
  final String contractAddress = "0x8C701E4df53Fd13b431fD5f1c7C7216b81535669"; //Address of deployed contract
  final String pk = "b591be22f682174d2ae1717da50f383a54b9bcebee972ec67f40a6ff4b1cb9af";//Private key of test account

  String apiResult1 = "";//These are part of testing
  String apiResult2 = "";
  String apiResult3 = "";

  Web3Client bcClient = Web3Client("HTTP://127.0.0.1:7545", Client()); //BlockChainClient

  final conValInputController = TextEditingController(); //Contract value input controller

  String nicerJSON(String j) //Just formats a JSON object using very crude methods for improved readability
  {
    int buffer = 0;
    String res = '';
    for (int i =0;i<j.length;i++)
    {
      if (j[i] == '{' || j[i] == '[' || j[i] == ',')
      {
        if (j[i] != ',')
        {
          res += '\n';
          for (int k =0;k<buffer;k++)
            res += ' ';
          buffer += 2;
        }
        res += j[i] + '\n';
        for (int k =0;k<buffer;k++)
          res += ' ';
      }
      else if (j[i] == '}' || j[i] == ']')
      {
        res += '\n';
        buffer = max(0, buffer -2);
        for (int k =0;k<buffer;k++)
          res += ' ';
        res += j[i];
      }
      else if (j[i] == ':')
        res += ' ' + j[i] + ' ';
      else
        res += j[i];
    }
    return res;
  }

  void makeRequest1() async
  {
    /*Future<http.Response>*/Response res = await get(Uri.parse('https://reqres.in/api/products/3'));
  String result = res.body;

  setState(() {
    apiResult1= nicerJSON(result);//.substring(0, 10);//result;
  });
  }

  Future<DeployedContract> getContract() async {
    String abi = await rootBundle.loadString("Assets/abi.json");
    final theContract = DeployedContract(ContractAbi.fromJson(abi, "SCV"), EthereumAddress.fromHex(contractAddress)); //Contract from ID
    return theContract;
  }

  Future<List<dynamic>> makeReadCall(String function, List<dynamic> args) async {
    final theContract = await getContract();
    final fun = theContract.function(function);
    List<dynamic> theResult = await bcClient.call(contract: theContract,function: fun, params: args);
    return theResult;
  }

  Future<String> makeWriteCall(String funct, List<dynamic> args) async {
    EthPrivateKey cred = EthPrivateKey.fromHex(pk); //Credentials from private key
    final theContract = await getContract();
    final fun = theContract.function(funct);
    final theResult = await bcClient.sendTransaction(cred, Transaction.callContract(contract: theContract, function: fun, parameters: args));
    return theResult;
  }

  void makeRequest2() async //Testing reading a value
  {
  //EthereumAddress addr = EthereumAddress.fromHex(address); //Used later for.... something
    List<dynamic> theResult = await makeReadCall("getData", []);
    final result = theResult[0];

    print (result);

    setState(() {
      apiResult2= result.toString();//nicerJSON(result);//result.substring(0, 10);//result;
    });
  }

  void makeRequest3() async //Testing transactions
  {

    try {
      BigInt par = BigInt.parse(conValInputController.text); //BigInt.from(5);
      //print ('None: ' + par.toString());

      String theResult = await makeWriteCall("setData", [par]);
      print("Here");
      setState(() {
        apiResult3 = "Contract returned: " +
            theResult;
      });
    }
    on Exception catch (_) {
      showDialog(context: context, builder: (context) {
         return AlertDialog(
         content: Text('Please use a valid number'),
          );
      });
    }
  }

  @override
  Widget build(BuildContext context) {

    return Scaffold(
      appBar: AppBar(
        title: Text('Contract Tests'),
        centerTitle: true,
        backgroundColor: Colors.deepPurple,
      ),
      body: Row(mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: <Widget>[
            Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.start,
                children: <Widget>[
                  Text(
                    'API Demo 1:', style: TextStyle(color: Colors.white, fontSize: 60),
                  ),
                  OutlinedButton(
                      onPressed: makeRequest1,
                      child: Text("Make API Call", style: TextStyle(color: Colors.white, fontSize: 20)),
                      style: OutlinedButton.styleFrom(side: BorderSide(color: Colors.white)),
                  ),
                  Container(
                    // width: 125,
                    child: SelectableText(apiResult1,
                      style: TextStyle(color: Colors.white, fontSize: 20),
                    ),
                  )
                ],
              ),
            ),
            Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.start,
                children: <Widget>[
                  Text(
                    'API Demo 2:',
                    style: TextStyle(color: Colors.white, fontSize: 60),
                  ),
                  OutlinedButton(
                    onPressed: makeRequest2,
                    child: Text("Get value in contract", style: TextStyle(color: Colors.white, fontSize: 20)),
                    style: OutlinedButton.styleFrom(side: BorderSide(color: Colors.white)),
                  ),
                  Container(
                    child: SelectableText(apiResult2,
                      style: TextStyle(color: Colors.white, fontSize: 20),
                    ),
                  )
                ],
              ),
            ),
            Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.start,
                children: <Widget>[
                  Text(
                    'API Demo 3:',
                    style: TextStyle(color: Colors.white, fontSize: 60),
                  ),
                Padding(
                  padding: EdgeInsets.symmetric(horizontal: 8, vertical: 16),
                  child:Container(
                    width: 100,
                    child:TextField(
                       controller: conValInputController,keyboardType: TextInputType.number,
                       inputFormatters: <TextInputFormatter>[
                         FilteringTextInputFormatter.digitsOnly,
                       ],
                    ),
                  ),
                ),
                  OutlinedButton(
                    onPressed: makeRequest3,
                    child: Text("Set value in contract", style: TextStyle(color: Colors.white, fontSize: 20)),
                    style: OutlinedButton.styleFrom(side: BorderSide(color: Colors.white)),
                  ),
                  SingleChildScrollView(
                  child: Container(
                    child: SelectableText(apiResult3,
                      style: TextStyle(color: Colors.white, fontSize: 20),
                    ),
                   )
                  ),
                ],
              ),
            ),
          ]
      ),
    );
  }
}

