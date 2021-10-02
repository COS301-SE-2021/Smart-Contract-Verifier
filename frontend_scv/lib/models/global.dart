//This class holds global variables for use by the entire app.

import 'dart:convert';
import 'dart:core';

import 'package:flutter/services.dart';
//import 'dart:async';

class Global {
  static String userAddress =
      ''; //The eth address of the current user. Can change.
  static String _contractId = '';
  //'0x35A4ccf302fb171e18B584e26cd3c0fB777F5Ca6'; //The identifier of the deployed contract. It is final, since it is basically hard-coded.

  static bool isJudge = false;

  static Future<String> getContractId(String id) async {
    String jsn = await rootBundle.loadString('JSON/address.json');
    try {
      _contractId = jsonDecode(jsn)[id];
    } catch (e) {
      //Invalid json
      print(e);
      return '';
    }
    return _contractId;
  }


  ///Converts an input string to a string of base64
  static String stringToBase64(String input) {
    Codec<String, String> stringToBase64 = utf8.fuse(base64);
    return stringToBase64.encode(input);
  }

  ///Converts a string of B64 to the original string
  static String base64ToString(String input) {
    Codec<String, String> stringToBase64 = utf8.fuse(base64);
    return stringToBase64.decode(input);
  }

  static String apiToken = '';
}
