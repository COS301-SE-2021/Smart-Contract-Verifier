import 'dart:convert';
import 'package:flutter/foundation.dart';
import 'package:http/http.dart' as http;
import '../models/http_exception.dart';

class Contract with ChangeNotifier {
  final String contractId; //agreementID
  final String durationId;
  String paymentId;
  final String partyA;
  final String partyB;
  final String createdDate;
  String sealedDate;
  bool movedToBlockchain;
  List<dynamic> conditions = ['empty']; //TODO

  final String title;
  final String description;
  final double price;
  final String imageUrl;
  final String partyBId;
  bool isFavorite;

  Contract({
    this.contractId,
    this.durationId,
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
    this.partyBId,
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

  void _setFavValue(bool newValue) {
    isFavorite = newValue;
    notifyListeners();
  }

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
}
