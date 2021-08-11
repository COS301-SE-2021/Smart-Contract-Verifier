//This file will contain a class containing methods needed to interact with the smart contract stored on the blockchain.
//The directory will hold similar classes, e.g. for Metamask communication.

import 'dart:async';

import 'package:flutter/services.dart';
import 'package:http/http.dart';
import 'package:web3dart/web3dart.dart';
import 'wallet.dart';
import '../../models/global.dart';

class SmartContract {
  final Web3Client _smC =
      Web3Client('http://localhost:8545', Client()); //smC = Smart Contract
  WalletInteraction _wallet = WalletInteraction();

  //Should this be called every time? Or should it only be loaded once....
  //Metamask will automatically detect multiple requests, but I am not certain about loading the abi.
  Future<DeployedContract> _getContract() async {

    String abi = await rootBundle
         .loadString("JSON/_src_Verifier_sol_Verifier.abi");
    await _wallet.metamaskConnect(); //Request metamask connection

    final theContract = DeployedContract(ContractAbi.fromJson(abi, "SCV"),
        EthereumAddress.fromHex(await Global.getContractId()));
    return theContract;
  }

  Future<List<dynamic>> makeReadCall(String function, List<dynamic> args) async {
    //Read from contract
    final theContract = await _getContract();
    final fun = theContract.function(function);
    List<dynamic> theResult =
        await _smC.call(contract: theContract, function: fun, params: args);
    return theResult;
  }

  Future<String> makeWriteCall(String funct, List<dynamic> args) async {
    //Write to contract

    final theContract = await _getContract();
    final fun = theContract.function(funct);

    final theResult = await _smC.sendTransaction(
        _wallet.getCredentials(),
        Transaction.callContract(
            contract: theContract, function: fun, parameters: args));

    print (theResult); //Debug
    return theResult;
  }

  Future<ContractEvent> getEvent(String ev) async { //Get an event from the contract
    final con = await _getContract();
    final event = con.event(ev);
    return event;
  }

  //This is used to detect the event emitted by creating a contract. This can be made more general, more thought is needed.
  Future<StreamSubscription> getCreationSubscription() async {

    final con = await _getContract();
    final ev = con.event('CreateAgreement'); //Revise

    //Function thing = Function(void Function);

    final res = _smC
        .events(FilterOptions.events(contract: con, event: ev))
        .take(1)
        .listen((event) {
      final decoded = ev.decodeResults(event.topics, event.data);

      final partyA = decoded[0] as EthereumAddress;
      final partyB = decoded[1] as EthereumAddress;
      final id = decoded[2] as BigInt;

      print('Contract $id between $partyA and $partyB');
    });

    return res;

  }

}
