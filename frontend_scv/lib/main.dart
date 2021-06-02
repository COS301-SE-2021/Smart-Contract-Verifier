import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
//import 'dart:math';
import 'package:http/http.dart'; // as http;
import 'package:web3dart/web3dart.dart';
import 'dart:async';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  // FRONTEND_DEV BRANCH
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Smart Contract Verifier',
      theme: ThemeData(
        // This is the theme of your application.
        //
        // Try running your application with "flutter run". You'll see the
        // application has a blue toolbar. Then, without quitting the app, try
        // changing the primarySwatch below to Colors.green and then invoke
        // "hot reload" (press "r" in the console where you ran "flutter run",
        // or simply save your changes to "hot reload" in a Flutter IDE).
        // Notice that the counter didn't reset back to zero; the application
        // is not restarted.
        primarySwatch: Colors.cyan,
        scaffoldBackgroundColor: Colors.grey,
      ),
      home: MyHomePage(title: 'Dashboard'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key? key, required this.title}) : super(key: key);

  // This widget is the home page of your application. It is stateful, meaning
  // that it has a State object (defined below) that contains fields that affect
  // how it looks.

  // This class is the configuration for the state. It holds the values (in this
  // case the title) provided by the parent (in this case the App widget) and
  // used by the build method of the State. Fields in a Widget subclass are
  // always marked "final".

  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {

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

  int tryVal  = -1;
  BigInt sendVal = BigInt.from(0);

  Web3Client bcClient = Web3Client("HTTP://127.0.0.1:8545", Client()); //BlockChainClient

  final conValInputController = TextEditingController(); //Contract value input controller
  final pkInputController =  TextEditingController(); //Private key value input controller
  final address2InputController =  TextEditingController(); //Second address value input controller
  final contractAddressInputController = TextEditingController(); //Input the contract address



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
    }
    on Exception catch (exception) { //Failure to parse
      showDialog(
          context: context,
          builder: (context) {
            return AlertDialog(
              content: Text(exception.toString()),
            );
          });

      return;
    }
    String theResult = await makeWriteCall("createAgreement", [EthereumAddress.fromHex(address2), sendVal]);
    setState(() {
      apiResult1 = "Contract returned: " + theResult;
    });

  }

  void acceptAgreement() async{
    try {
      loadValues();
    }
    on Exception catch (exception) { //Failure to parse
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
        "acceptAgreement", [sendVal]);
    setState(() {
      apiResult2 = "Contract returned: " + theResult;
    });
  }

  void closeAgreement() async{
    try {
      loadValues();
    }
    on Exception catch (exception) { //Failure to parse
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
        "closeAgreement", [sendVal]);
    setState(() {
      apiResult3 = "Contract returned: " + theResult;
    });
  }



  @override
  Widget build(BuildContext context) {
    // This method is rerun every time setState is called, for instance as done
    // by the _incrementCounter method above.
    //
    // The Flutter framework has been optimized to make rerunning build methods
    // fast, so that you can just rebuild anything that needs updating rather
    // than having to individually change instances of widgets.
    return Scaffold(
      appBar: AppBar(
        title: Text('Contract Tests'),
        centerTitle: true,
        backgroundColor: Colors.deepPurple,
      ),
      body:Column(

        children: <Widget>[
          Row (
            children:<Widget> [ Padding(
              padding: EdgeInsets.symmetric(horizontal: 8, vertical: 16),
              child: Container(
                width: 300,
                child: TextField(
                  controller: pkInputController,
                  decoration: InputDecoration(
                      border: OutlineInputBorder(borderSide: BorderSide(color: Colors.white)),
                      hintText: 'Enter your private key pls'
                  ),
                ),
              ),
            ),
              Padding(
                padding: EdgeInsets.symmetric(horizontal: 8, vertical: 16),
                child: Container(
                  width: 300,
                  child: TextField(
                    controller: address2InputController,
                    decoration: InputDecoration(
                        border: OutlineInputBorder(),
                        hintText: 'Enter the second address'
                    ),
                  ),
                ),
              ),

              Padding(
                padding: EdgeInsets.symmetric(horizontal: 8, vertical: 16),
                child: Container(
                  width: 300,
                  child: TextField(
                    controller: contractAddressInputController,
                    decoration: InputDecoration(
                        border: OutlineInputBorder(),
                        hintText: 'Enter the Contract address'
                    ),
                  ),
                ),
              ),
              Padding(
                padding: EdgeInsets.symmetric(horizontal: 8, vertical: 16),
                child: Container(
                  width: 300,
                  child: TextField(

                    controller: conValInputController,
                    keyboardType: TextInputType.number,
                    inputFormatters: <TextInputFormatter>[
                      FilteringTextInputFormatter.digitsOnly,
                    ],
                    decoration: new InputDecoration(
                      hintText: "Enter value for processing",
                      border: new OutlineInputBorder(
                          borderSide: new BorderSide(color: Colors.white)),
                    ),
                  ),
                ),
              ),
            ],
          ),
          Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: <Widget>[
                Center(
                  child: Container(
                   /* width: 50,
                    height: 50,*/
                    child:Column(
                    mainAxisAlignment: MainAxisAlignment.start,
                    children: <Widget>[
                      Text(
                        'Open:',
                        style: TextStyle(color: Colors.white, fontSize: 60),
                      ),
                      OutlinedButton(
                        onPressed: openAgreement,
                        child: Text("Open",
                            style: TextStyle(color: Colors.white, fontSize: 20)),
                        style: OutlinedButton.styleFrom(
                            side: BorderSide(color: Colors.white)),
                      ),
                      Container(
                        // width: 125,
                        child: SelectableText(
                          apiResult1,
                          style: TextStyle(color: Colors.white, fontSize: 20),
                        ),
                      )
                    ],
                  ),
                  ),
                ),
                Center(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.start,
                    children: <Widget>[
                      Text(
                        'Accept:',
                        style: TextStyle(color: Colors.white, fontSize: 60),
                      ),
                      OutlinedButton(
                        onPressed: acceptAgreement,
                        child: Text("Get value in contract",
                            style: TextStyle(color: Colors.white, fontSize: 20)),
                        style: OutlinedButton.styleFrom(
                            side: BorderSide(color: Colors.white)),
                      ),
                      Container(
                        child: SelectableText(
                          apiResult2,
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
                        'Close:',
                        style: TextStyle(color: Colors.white, fontSize: 60),
                      ),

                      OutlinedButton(
                        onPressed: closeAgreement,
                        child: Text("Set value in contract",
                            style: TextStyle(color: Colors.white, fontSize: 20)),
                        style: OutlinedButton.styleFrom(
                         side: BorderSide(color: Colors.white)),
                      ),
                      SingleChildScrollView(
                          child: Container(
                            child: SelectableText(
                              apiResult3,
                              style: TextStyle(color: Colors.white, fontSize: 20),
                            ),
                          )),
                    ],
                  ),
                ),
              ]),
        ],
      ),
    );
  }
}


//$$$$$$$$$$$$$$$$$$$$$$$$$$$$

