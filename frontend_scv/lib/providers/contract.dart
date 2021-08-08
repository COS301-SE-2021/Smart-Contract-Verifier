import 'dart:convert';
import 'package:flutter/foundation.dart';
import 'package:http/http.dart' as http;
import 'package:unison/providers/condition.dart';
import '../models/http_exception.dart';
import 'package:flutter/material.dart';

class Contract with ChangeNotifier {
  String contractId; //agreementID
  String paymentId;
  String partyA;
  String partyB;
  String createdDate;
  String sealedDate;
  bool movedToBlockchain;
  List<dynamic> conditions = ['empty']; //TODO Handle empty/initial conditions

  String title;
  String description;
  double price;
  String imageUrl;
  String partyBId;
  bool isFavorite;

  Contract({
    this.contractId,
    this.paymentId,
    this.partyA,
    this.partyB,
    this.createdDate,
    this.sealedDate,
    this.movedToBlockchain,
    this.conditions,
    this.title,
    this.description,
    this.price,
    this.imageUrl,
    this.partyBId, //RC: What is partyB vs partyBID?
    this.isFavorite = false,
  }); // Contract({
  //   @required this.contractId,
  //   @required this.title,
  //   @required this.description,
  //   @required this.price,
  //   @required this.imageUrl,
  //   @required this.partyBId,
  //   this.isFavorite = false,
  // }); //not final because it will be changing

  //JSON constructor. Uses response from getAgreement. RC: Should be revised
  Contract.fromJson(Map<String, dynamic> jsn) {
    contractId = jsn['AgreementID'];
    partyA = jsn['PartyA']['publicWalletID'];
    partyB = jsn['PartyB']['publicWalletID'];
    createdDate = jsn['CreatedDate'];
    sealedDate = jsn['SealedDate'];
    //status = json['status'],
    movedToBlockchain = jsn['MovedToBlockChain'];
    description = jsn['AgreementDescription'];
    imageUrl = jsn['AgreementImageURL'];
    partyBId = '';
    price = 0;
    title = jsn['AgreementTitle'];
    // contractId = '';
    conditions = []; //jsn['Conditions'];
    for (int i = 0; i < jsn['Conditions'].length; i++) {
      conditions.add(Condition.fromJson(jsn['Conditions'][i]));
    }
  }
  //To JSON. RC: Subject to the great field discussion
  Map<String, dynamic> toJson() => {
        'AgreementID': contractId,
        'PartyA': partyA,
        'PartyB': partyB,
        'CreatedDate': createdDate,
        'SealedDate': sealedDate,
        'MovedToBlockChain': movedToBlockchain,
        'Conditions': conditions,
        'Title': title,
        'Description': description,
        'ImageUrl': imageUrl,
      };

  void _setFavValue(bool newValue) {
    isFavorite = newValue;
    notifyListeners();
  }

  //RC: Could this be stored locally rather than on the db? Since it's a user preference
  //KC: The idea was incase they have multiple devices
  //    (like mobile and web or 2 different web machines, their favorites would still be there
  //    favorites regardless of what device or machine they are on)
  Future<void> toggleFavoriteStatus(String token, String userId) async {
    final oldStatus = isFavorite;
    _setFavValue(!isFavorite);
    final url =
        'https://capstone-testing-a7ee4-default-rtdb.firebaseio.com/userFavorites/$userId/$contractId.json?auth=$token';
    try {
      final response = await http.put(Uri.parse(url),
          body: json.encode(
            isFavorite,
          ));
      if (response.statusCode >= 400)
        throw HttpException('Couldn\'t toggle favorite');
    } catch (error) {
      _setFavValue(oldStatus);
    }
  }

  String toString() {
    //A ToString method for debugging purposes
    String ret = 'ID: ' + contractId + '\n';
    ret += 'Party A: ' + partyA.toString() + '\n';
    ret += 'Party B: ' + partyB.toString() + '\n';

    return ret;
  }
}
