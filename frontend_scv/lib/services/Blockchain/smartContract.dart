//This file will contain a class containing methods needed to interact with the smart contract stored on the blockchain.
//The directory will hold similar classes, e.g. for Metamask communication.

import 'package:flutter/services.dart';
import 'package:http/http.dart';
import 'package:web3dart/web3dart.dart';
import 'wallet.dart';

final String bNet = ''; //Url to blockchain. Preferably, this would be a global variable in a file of globals.

class SmartContract {

  final Web3Client smC = Web3Client(bNet, Client()); //smC = Smart Contract
  WalletInteraction wallet = WalletInteraction();

  //Should this be called every time? Or should it only be loaded once....
  //Metamask will automatically detect multiple requests, but I am not certain about loading the abi.
  Future<DeployedContract> _getContract() async {
    String abi = await rootBundle.loadString("/assets/JSON/abi.json"); //Load contract from json
    await wallet.metamaskConnect(); //Request metamask connection

    final theContract = DeployedContract(ContractAbi.fromJson(abi, "SCV"),
        wallet.getCredentials().address);
    return theContract;
  }

  Future<List<dynamic>> makeReadCall(String function, List<dynamic> args) async { //Read from contract
    final theContract = await _getContract();
    final fun = theContract.function(function);
    List<dynamic> theResult =
    await smC.call(contract: theContract, function: fun, params: args);
    return theResult;
  }

  Future<String> makeWriteCall(String funct, List<dynamic> args) async { //Write to contract

    final theContract = await _getContract();
    final fun = theContract.function(funct);
    final theResult = await smC.sendTransaction(
        wallet.getCredentials(),
        Transaction.callContract(
            contract: theContract, function: fun, parameters: args));
    return theResult;
  }
  
}