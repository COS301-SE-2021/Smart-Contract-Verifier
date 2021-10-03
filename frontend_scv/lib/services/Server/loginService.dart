//This class is for the initial login, and the flow that will come with it.

import 'dart:typed_data';
import 'package:unison/services/Blockchain/wallet.dart';
import '../../models/global.dart';
import 'apiResponse.dart';
import 'backendAPI.dart';

class LoginService {

  ApiInteraction _api = ApiInteraction();
  WalletInteraction _wallet = WalletInteraction();

  ///Get a new JWT
  Future<void> refreshToken() async { //Starts the login procedure to get a token to use in backend api requests
    await login();
  }


  ///Attempt to add a user to the server. If the user already exists, no action is taken
  Future<void> tryAddUser([String id]) async { //This attempts to add a new user to the DB. Failure means the user already exists

    id ??= Global.userAddress; //Defaults to current user
    try {
      final res = await _api.postData('/user', {'WalletID': id, 'Alias' : 'Nothing'});

    } on Exception catch (e) {
      print(e);
    }

  }

  ///Login using Metamask
  Future<void> login() async {
    ApiResponse nonceRes = await _api.getData('/user/${Global.userAddress}');

    if (!nonceRes.successful) {
      throw 'Could not get challenge to sign. Details:\n' + nonceRes.errorMessage;
    }
    //Extract and sign nonce
    String nonce = nonceRes.result['UnsignedNonce'];

    var signed;
    try {
      signed = await _wallet.personalSign(nonce);
    } catch (e) {
      throw 'Could not sign challenge.';
    }

    //Send back signed nonce
    var body = {'SignedNonce' : signed};
    ApiResponse loginRes = await _api.postData('/user/${Global.userAddress}', body);
    if (!loginRes.successful) {
      throw 'Challenge was not signed correctly. Details:\n' + loginRes.errorMessage;
    }

    Global.apiToken = loginRes.result['JwtToken']; //Extract from result
  }


}