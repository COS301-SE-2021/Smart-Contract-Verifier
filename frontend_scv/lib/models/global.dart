//This class holds global variables for use by the entire app.

import 'dart:convert';
import 'package:flutter/services.dart';
//import 'dart:async';

class Global {
  static String userAddress =
      ''; //The eth address of the current user. Can change.
  static String _contractId = '';
  static bool _loaded = false;
      //'0x35A4ccf302fb171e18B584e26cd3c0fB777F5Ca6'; //The identifier of the deployed contract. It is final, since it is basically hard-coded.

  static Future<String> getContractId() async {

    if (!_loaded) { //Only load JSON once
      String jsn = await rootBundle.loadString('JSON/address.json');
      try {
        _contractId = jsonDecode(jsn)['Verifier'];
        _loaded = true;
      } catch (e) {
        //Invalid json
        print (e);
      }
    }

    return _contractId;
  }

  static String apiToken = '';
}
