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
      final res =await _api.postData('/user', {'WalletID': id, 'Alias' : 'Nothing'});
      print ('Res: ' + res.toString());

    } on Exception catch (e) {
      print(e);
      throw(e);
    }

  }

  ///Login using Metamask
  Future<bool> login() async {
    ApiResponse nonceRes = await _api.getData('/user/${Global.userAddress}');

    //print ('Gotten none');
    //Extract and sign nonce
    String nonce = nonceRes.result['UnsignedNonce'];

    //print ('About to sign: ' + nonce);
    final signed = await _wallet.personalSign(nonce);

    //print ('Signed: ' +signed.toString());

    //Send back signed nonce
    var body = {'SignedNonce' : signed};
    ApiResponse loginRes = await _api.postData('/user/${Global.userAddress}', body);
    Global.apiToken = loginRes.result['JwtToken']; //Extract from result
    return loginRes.successful; //Login success
  }


}