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
  final String contractAddress = "0x4C4a0F204506dE9c9680b1b4c54F8f5f7728067c"; //Address of deployed contract

  String apiResult1 = "";//These are part of testing
  String apiResult2 = "";
  String apiResult3 = "";


  //Client htClient = Client();
  Web3Client bcClient = Web3Client("HTTP://127.0.0.1:7545", Client()); //BlockChainClient

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

  Future<List<dynamic>> makeCall(String function, List<dynamic> args) async {
    String abi = await rootBundle.loadString("Assets/abi.json");
    final theContract = DeployedContract(ContractAbi.fromJson(abi, "SCV"), EthereumAddress.fromHex(contractAddress)); //Contract from ID
    final fun = theContract.function(function);
    List<dynamic> theResult = await bcClient.call(contract: theContract,function: fun, params: args);
    return theResult;
  }

  void makeRequest2() async
  {
  //EthereumAddress addr = EthereumAddress.fromHex(address); //Used later for.... something
    List<dynamic> theResult = await makeCall("getData", []);
    final result = theResult[0];

    print (result);

    setState(() {
      apiResult2= result.toString();//nicerJSON(result);//result.substring(0, 10);//result;
    });
  }

  void makeRequest3() async
  {
    BigInt par = BigInt.from(5);
    List<dynamic> theResult = await makeCall("setData", [par]);
    print ("Here");                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
    String result = "";//theResult[0];

    for (int i =0;i<theResult.length;i++)
    {
      print(theResult[i]);
    }

    setState(() {
      apiResult3= result;//nicerJSON(result);//result.substring(0, 10);//result;
    });
  }

/*
  @override
  void initState()
  {
    super.initState();
    httpClient = Client();
  }*/

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
                      child: Text("Make Call", style: TextStyle(color: Colors.white, fontSize: 20)),
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
                    child: Text("Make Call", style: TextStyle(color: Colors.white, fontSize: 20)),
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
                  OutlinedButton(
                    onPressed: makeRequest3,
                    child: Text("Make Call", style: TextStyle(color: Colors.white, fontSize: 20)),
                    style: OutlinedButton.styleFrom(side: BorderSide(color: Colors.white)),
                  ),
                  Container(
                    child:SelectableText(apiResult3,
                      style: TextStyle(color: Colors.white, fontSize: 20),
                    ),
                  )
                ],
              ),
            ),
          ]
      ),
    );
  }
}

