import 'dart:convert';
import 'package:flutter/foundation.dart';
import 'package:http/http.dart' as http;
import '../models/http_exception.dart';

class Contract with ChangeNotifier {
  final String id;
  final String title;
  final String description;
  final double price;
  final String imageUrl;
  final String partyBId;
  bool isFavorite;

  Contract({
    @required this.id,
    @required this.title,
    @required this.description,
    @required this.price,
    @required this.imageUrl,
    @required this.partyBId,
    this.isFavorite = false,
  }); //not final because it will be changing

  void _setFavValue(bool newValue) {
    isFavorite = newValue;
    notifyListeners();
  }

  Future<void> toggleFavoriteStatus(String token, String userId) async {
    final oldStatus = isFavorite;
    _setFavValue(!isFavorite);
    final url =
        'https://capstone-testing-a7ee4-default-rtdb.firebaseio.com/userFavorites/$userId/$id.json?auth=$token';
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
