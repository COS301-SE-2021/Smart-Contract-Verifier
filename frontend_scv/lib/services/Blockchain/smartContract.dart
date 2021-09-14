//This file will contain a class containing methods needed to interact with the smart contract stored on the blockchain.
//The directory will hold similar classes, e.g. for Metamask communication.

import 'dart:async';

import 'package:flutter/services.dart';
import 'package:http/http.dart';
import 'package:web3dart/web3dart.dart';

import '../../models/global.dart';
import 'wallet.dart';

class SmartContract {
  static final Web3Client _smC =
      Web3Client('http://localhost:8545', Client()); //smC = Smart Contract
  static final WalletInteraction _wallet = WalletInteraction();

  String conAbi;
  String contractName;

  DeployedContract _contract;
  bool _loaded = false;

  SmartContract(String ab, String name) {
    //Create an instance for a specific abi
    conAbi = ab;
    contractName = name;
  }

  //Should this be called every time? Or should it only be loaded once....
  //Metamask will automatically detect multiple requests, but I am not certain about loading the abi.
  Future<DeployedContract> _getContract() async {
    if (!_loaded) {
      //Only load contract once
      String abi = await rootBundle.loadString(conAbi);
      // await _wallet.metamaskConnect(); //Request metamask connection (May not be necessary at this stage)

      _contract = DeployedContract(ContractAbi.fromJson(abi, "SCV"),
          EthereumAddress.fromHex(await Global.getContractId(contractName)));

      _loaded = true;
    }

    return _contract;
  }

  Future<List<dynamic>> makeReadCall(
      String function, List<dynamic> args) async {
    //Read from contract
    final theContract = await _getContract();
    final fun = theContract.function(function);
    List<dynamic> theResult = await _smC.call(contract: theContract, function: fun, params: args);

    return theResult;
  }

  Future<String> makeWriteCall(String funct, List<dynamic> args) async {
    //Write to contract

    final theContract = await _getContract();
    final fun = theContract.function(funct);

    _wallet.getCredentials();

    var theResult = await _smC.sendTransaction(
          _wallet.getCredentials(),
          Transaction.callContract(
              contract: theContract, function: fun, parameters: args));

    //print('First res: ' +theResult.toString()); //Debug
    return theResult;
  }

  Future<ContractEvent> getEvent(String ev) async {
    //Get an event from the contract
    final con = await _getContract();
    final event = con.event(ev);
    return event;
  }

}
