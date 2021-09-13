import 'dart:convert';
import 'dart:math';

import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:unison/models/condition.dart';

import 'global.dart';
import 'http_exception.dart';

class Contract with ChangeNotifier {
  String contractId; //agreementID
  BigInt blockchainId; //TODO: remove ples
  String partyA;
  String partyB;
  DateTime createdDate;
  DateTime sealedDate;
  bool movedToBlockchain;
  List<Condition> conditions; //TODO Handle empty/initial conditions
  String title;

  String description;
  String payingUser;
  double paymentAmount;
  String paymentID; //Used to cross-reference with all conditions
  String imageUrl;
  //String partyBId;
  BigInt duration;
  String durationID;

  Contract({
    this.contractId,
    this.partyA,
    this.partyB,
    this.createdDate,
    this.sealedDate,
    this.movedToBlockchain,
    this.conditions,
    this.title,
    this.description,
    this.paymentAmount,
    this.imageUrl,
  });

  //JSON constructor. Uses response from getAgreement. RC: Should be revised
  Contract.fromJson(Map jsn) {
    //TODO: publicWalletID will be upper case soon
    contractId = jsn['AgreementID'];
    partyA = jsn['PartyA']['publicWalletID'];
    partyB = jsn['PartyB']['publicWalletID'];
    // createdDate = null;
    // sealedDate = null;
    //status = json['status'],

    //Try to get items that may not exist yet
    try {
      duration = BigInt.from(jsn['DurationCondition']['Amount']);
    } catch (_) {}
    try {
      paymentAmount = jsn['PaymentCondition']['Amount'];
    } catch (_) {}
    try {
      createdDate = DateTime.parse(jsn['CreatedDate']);
      sealedDate = DateTime.parse(jsn['SealedDate']);
    } catch (_) {}

    try {
      blockchainId = BigInt.from(jsn['BlockChainID']);
      movedToBlockchain = true;
    } catch (error) {
      print('BC ID ERR: ' + error.toString());
      // blockchainId = BigInt.from(0);
      movedToBlockchain = false;
    }
    movedToBlockchain = jsn['MovedToBlockchain'];
    description = jsn['AgreementDescription'];
    imageUrl = jsn['AgreementImageURL'];
    //partyBId = '';
    title = jsn['AgreementTitle'];
    conditions = (jsn['Conditions'])
        .map<Condition>((i) => Condition.fromJson(i))
        .toList();

    var payment = jsn['PaymentCondition'];
    try {

        paymentID = payment['ID'];
        paymentAmount = payment['Amount'];
        payingUser = payment['Payer'];

    } catch (_) {} //Payment condition not set

    var durationCond = jsn['DurationCondition'];
    try {

      durationID = durationCond['ID'];
      duration = durationCond['Amount'];

    } catch (_) {} //Duration condition not set

  }

  Map<String, String> toJson() {
    return {
      //This is used in the initial save to the backend, hence not all fields being present.
    //  'PartyA': partyA,
      'PartyB': partyB,
      'AgreementTitle': title,
      'AgreementDescription': description,
      'AgreementImageURL': imageUrl,
    };
  }

  // Map<String, String> toJson() => {
  //       //This is used in the initial save to the backend, hence not all fields being present.
  //       'PartyA': partyA,
  //       'PartyB': partyB,
  //       'AgreementTitle': title,
  //       'AgreementDescription': description,
  //       'AgreementImageURL': imageUrl,
  //     };

  Map<String, String> toJsonChain() => {
        //This is used in the save to the blockchain.
        'PartyA': partyA,
        'PartyB': partyB,
        'AgreementTitle': title,
        'AgreementDescription': description,
        'AgreementImageURL': imageUrl,
      };

  void setBlockchainID(BigInt id) {
    blockchainId = id;
    movedToBlockchain = true;
  }

  void setDuration(BigInt d) {
    duration = d;
  }

  void _setFavValue(bool newValue) {
    // isFavorite = newValue;
    notifyListeners();
  }

  ///Generate a string of conditions to send to the blockchain upon agreement creation.
  String dataToChain() {

    String ret = Global.stringToBase64(title) + '#' + Global.stringToBase64(description) + '#{';
     for (Condition i in conditions) {
        ret += i.toChain();
     }

     ret += '}';
     return ret;
  }

 ///Generate an instance from the blockchain. This differs from a blockchainagreement, in that it should only be used to verify the state of conditions saved in the smart contract.
 Contract.fromChain(String data) {
    int hPos = data.indexOf('#');
    title = Global.base64ToString(data.substring(0, hPos));
    String next = data.substring(hPos+1);
    hPos = next.indexOf('#');
    description = Global.base64ToString(next.substring(0, hPos));
    next = next.substring(hPos+1);
    //This method to ensure the last '}' is used in the string. A better method may be used in the future.

    next = next.substring(next.indexOf('{') +1, next.lastIndexOf('}'));
    //Next is now a list of conditions, each enclosed in []
    int count = min('['.allMatches(next).length, ']'.allMatches(next).length);

    //This should be safe. The data will be encoded before saving on blockchain.
    conditions = [];
    for (int i =0;i<count;i++) {
        String item = next.substring(next.indexOf('[') +1, next.indexOf(']'));
        conditions.add(Condition.fromChainData(item));
        next = next.substring(next.indexOf(']') +1);
    }

 }

  String toString() {
    //A ToString method for debugging purposes
    String ret = 'ID: ' + contractId + '\n';
    ret += 'Party A: ' + partyA.toString() + '\n';
    ret += 'Party B: ' + partyB.toString() + '\n';

    return ret;
  }

  ///A check to ensure the validity of an agreement saved on the blockchain.
  bool blockchainValid(Contract con) {
    //TODO: Maybe this could be done using a hash?

    bool ret = true;
    ret = con.title == title && con.description == description;

    //Check conditions. The two agreements must have their conditions in the same order.
    for (int i =0;i<conditions.length && ret;i++) {
        ret = ret && conditions[i].valid(con.conditions[i]);
    }

    return ret;

  }
}
