import 'dart:async'; //for timer
import 'package:flutter/widgets.dart';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';
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

  Future<void> metaMaskLoginSuccess() async {
    try {
      final cred = walletInteraction.getCredentials();
      _isAuth = true;
      _userWalletAddress = cred.address.toString();
      notifyListeners();
    } catch (error) {
      throw error;
    }
  }

  Future<void> metaMaskLogin() async {
    await walletInteraction.metamaskConnect();
    metaMaskLoginSuccess();
    notifyListeners();
    // print(walletInteraction.getCredentials().toString());
  }

  Future<void> logout() async {
    _userWalletAddress = null;
    _isAuth = false;
    notifyListeners();
    final prefs = await SharedPreferences.getInstance();
    // prefs.remove('userData');
    prefs.clear();
  }
}
