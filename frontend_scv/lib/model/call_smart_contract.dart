import 'dart:async';
import 'dart:math';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:http/http.dart'; // as http;
import 'package:web3dart/web3dart.dart';

class SmartContractRequest {
  final String privateKey;
  final String secondAddress;
  final String contractAddress;
  final String processingValue;

  Web3Client bcClient = Web3Client("HTTP://127.0.0.1:8545", Client());

  SmartContractRequest({
    required this.privateKey,
    required this.secondAddress,
    required this.contractAddress,
    required this.processingValue,
  });

  Future<String> makeWriteCall(String functionName, List<dynamic> args) async {
    EthPrivateKey cred =
        EthPrivateKey.fromHex(privateKey); //Credentials from private key
    final theContract = await getContract();
    final fun = theContract.function(functionName);
    final theResult = await bcClient.sendTransaction(
        cred,
        Transaction.callContract(
            contract: theContract, function: fun, parameters: args));
    return theResult;
  }

  Future<DeployedContract> getContract() async {
    String abi = await rootBundle.loadString("Assets/abi.json");
    final theContract = DeployedContract(ContractAbi.fromJson(abi, "SCV"),
        EthereumAddress.fromHex(contractAddress)); //Contract from ID
    return theContract;
  }

  void simplePrint() {
    print('Private Key: $privateKey\n'
        'Second Address: $secondAddress\n'
        'Contract Address: $contractAddress\n'
        'Processing Value: $secondAddress\n');
  }
}
