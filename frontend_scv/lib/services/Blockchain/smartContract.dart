//This file will contain a class containing methods needed to interact with the smart contract stored on the blockchain.
//The directory will hold similar classes, e.g. for Metamask communication.

import 'package:flutter/services.dart';
import 'package:http/http.dart';
import 'package:web3dart/web3dart.dart';
import 'wallet.dart';
import '../../providers/global.dart';


class SmartContract {

  final Web3Client _smC = Web3Client(Global.contractId, Client()); //smC = Smart Contract
  WalletInteraction _wallet = WalletInteraction();

  //Should this be called every time? Or should it only be loaded once....
  //Metamask will automatically detect multiple requests, but I am not certain about loading the abi.
  Future<DeployedContract> _getContract() async {
    String abi = await rootBundle.loadString("/assets/JSON/abi.json"); //Load contract from json
    await _wallet.metamaskConnect(); //Request metamask connection

    final theContract = DeployedContract(ContractAbi.fromJson(abi, "SCV"),
        _wallet.getCredentials().address);
    return theContract;
  }

  Future<List<dynamic>> makeReadCall(String function, List<dynamic> args) async { //Read from contract
    final theContract = await _getContract();
    final fun = theContract.function(function);
    List<dynamic> theResult =
    await _smC.call(contract: theContract, function: fun, params: args);
    return theResult;
  }

  Future<String> makeWriteCall(String funct, List<dynamic> args) async { //Write to contract

    final theContract = await _getContract();
    final fun = theContract.function(funct);
    final theResult = await _smC.sendTransaction(
        _wallet.getCredentials(),
        Transaction.callContract(
            contract: theContract, function: fun, parameters: args));
    return theResult;
  }
  
}