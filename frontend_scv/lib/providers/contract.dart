import 'dart:convert';
import 'package:flutter/foundation.dart';
import 'package:http/http.dart' as http;
import '../models/http_exception.dart';
import 'package:flutter/material.dart';

class Contract with ChangeNotifier {
  final String contractId; //agreementID
  final String durationId;
  String paymentId;
  final String partyA;
  final String partyB;
  final String createdDate;
  String sealedDate;
  bool movedToBlockchain;
  List<dynamic> conditions = ['empty']; //TODO Handle empty/initial conditions

  final String title;
  final String description;
  final double price;
  final String imageUrl;
  final String partyBId;
  bool isFavorite;

  Contract({
    this.contractId,
    this.durationId, //RC: Duration could be time
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
  Contract.fromJson(Map<String, dynamic> jsn)
      : contractId = jsn['agreementID'],
        //duration = json['duration'], //Should be durationID?
        durationId = '',//jsn['duration'],
        partyA = jsn['partyA']['publicWalletID'],
        partyB = jsn['partyB']['publicWalletID'],
        createdDate = jsn['createdDate'],
        sealedDate = jsn['sealedDate'],
        //status = json['status'],
        movedToBlockchain = jsn['movedToBlockChain'],
        description = jsn['agreementDescription'],
        imageUrl = jsn['agreementImageURL'],
        partyBId = '',
        price = 0,
        title = jsn['agreementTitle'],
        conditions = jsn['conditions'];

  //To JSON. RC: Subject to the great field discussion
  Map<String, dynamic> toJson() => {
        'agreementID': contractId,
        'duration': durationId,
        'PartyA': partyA,
        'PartyB': partyB,
        'createdDate': createdDate,
        'sealedDate': sealedDate,
        'movedToBlockChain': movedToBlockchain,
        'conditions': conditions,
        'title' : title,
        'description' : description,
        'imageUrl' : imageUrl,
      };

  void _setFavValue(bool newValue) {
    isFavorite = newValue;
    notifyListeners();
  }

  //RC: Could this be stored locally rather than on the db? Since it's a user preference
  Future<void> toggleFavoriteStatus(String token, String userId) async {
    final oldStatus = isFavorite;
    _setFavValue(!isFavorite);
    final url =
        'https://capstone-testing-a7ee4-default-rtdb.firebaseio.com/userFavorites/$userId/$contractId.json?auth=$token';
    try {
      final response = await http.put(url,
          body: json.encode(
            isFavorite,
          ));
      if (response.statusCode >= 400)
        throw HttpException('Couldn\'t toggle favorite');
    } catch (error) {
      _setFavValue(oldStatus);
    }
  }

  String toString() { //A ToString method for debugging purposes
    String ret = 'ID: ' + contractId + '\n';
    ret += 'Party A: ' + partyA.toString() + '\n';
    ret += 'Party B: ' + partyB.toString() + '\n';

    return ret;
  }

}
