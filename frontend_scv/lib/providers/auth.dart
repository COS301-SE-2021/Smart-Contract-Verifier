import 'dart:async'; //for timer

import 'package:flutter/widgets.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:unison/services/Server/loginService.dart';

import '../services/Blockchain/wallet.dart';

class Auth with ChangeNotifier {
  //Setup wallet interaction object
  WalletInteraction walletInteraction = WalletInteraction();

  String _userWalletAddress;
  bool _isAuth = false;

  bool get isAuth {
    return _isAuth;
  }

  String get userWalletAddress {
    return _userWalletAddress;
  }

  Future<void> metaMaskLogin() async {
    LoginService loginService = LoginService();
    try {
      await walletInteraction.metamaskConnect();
    } catch (error) {
      throw (error);
    }
    final cred = walletInteraction.getCredentials();
    _isAuth = true;
    _userWalletAddress = cred.address.toString();
    loginService.tryAddUser();

    notifyListeners();
    // print(walletInteraction.getCredentials().toString());
  }

  Future<void> logout() async {
    _userWalletAddress = null;
    _isAuth = false;
    walletInteraction.metamaskDisconnect();
    notifyListeners();
    final prefs = await SharedPreferences.getInstance();
    // prefs.remove('userData');
    prefs.clear();
  }
}
