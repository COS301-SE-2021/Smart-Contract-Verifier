import 'dart:convert';
import 'dart:async'; //for timer
import 'package:flutter/widgets.dart';
import 'package:http/http.dart' as http;
import '../models/http_exception.dart';

class TestApi with ChangeNotifier {
  String data;

  Future<void> getHello() async {
    final url = 'http://localhost:8080/negotiation/hello';
    try {
      final response = await http.get(
        url,
      );
      final responseData = json.decode(response.body);
      if (responseData['error'] != null) {
        throw HttpException(responseData['error']['message']);
      }
      notifyListeners();
      print("response: " + responseData);
    } catch (error) {
      throw error;
    }
  }
}
