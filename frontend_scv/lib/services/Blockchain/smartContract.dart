//This file will contain a class containing methods needed to interact with the smart contract stored on the blockchain.
//The directory will hold similar classes, e.g. for Metamask communication.

import 'package:flutter/services.dart';
import 'package:http/http.dart';
import 'package:web3dart/web3dart.dart';
import 'wallet.dart';

final String bNet = ''; //Url to blockchain. Preferably, this would be a global variable in a file of globals.

class SmartContract {

  final Web3Client smC = Web3Client(bNet, Client()); //smC = Smart Contract
  WalletInteraction metaMask = WalletInteraction();

  Future<DeployedContract> getContract() async {
    String abi = await rootBundle.loadString("Assets/abi.json");
    final theContract = DeployedContract(ContractAbi.fromJson(abi, "SCV"),
        metaMask.getCredentials().address);
    return theContract;
  }

  Future<List<dynamic>> makeReadCall(
      String function, List<dynamic> args) async {
    final theContract = await getContract();
    final fun = theContract.function(function);
    List<dynamic> theResult =
    await smC.call(contract: theContract, function: fun, params: args);
    return theResult;
  }

  Future<String> makeWriteCall(String funct, List<dynamic> args) async {

    final theContract = await getContract();
    final fun = theContract.function(funct);
    final theResult = await smC.sendTransaction(
        metaMask,
        Transaction.callContract(
            contract: theContract, function: fun, parameters: args));
    return theResult;
  }
  
}