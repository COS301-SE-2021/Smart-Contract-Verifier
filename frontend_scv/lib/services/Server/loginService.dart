//This class is for the initial login, and the flow that will come with it.

import 'dart:typed_data';
import 'package:unison/services/Blockchain/wallet.dart';
import '../../models/global.dart';
import 'apiResponse.dart';
import 'backendAPI.dart';

class LoginService {

  ApiInteraction _api = ApiInteraction();
  WalletInteraction _wallet = WalletInteraction();

  Future<void> refreshToken() async { //Starts the login procedure to get a token to use in backend api requests

    Global.apiToken = ''; //Will eventually be used in all following requests to backend.

  }

  Future<void> allowToken() async {

    //Sets the contract allowance on first login. This should only be done once, as it costs money.
    //TODO: Temporary solution.
    //await _jS.setContractAllowance();
  }

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

  ///Log he user in with the server
  Future<bool> login() async {
    ApiResponse nonceRes = await _api.getData('/user/${Global.userAddress}');

    //Extract and sign nonce
    BigInt nonce = nonceRes.result['UnsignedNonce'];
    final signed = await _wallet.personalSign(nonce.toString());

    print ('Signed: ' +signed.toString());

    //Send back signed nonce
    var body = {'SignedNonce' : signed};
    ApiResponse loginRes = await _api.postData('/user/${Global.userAddress}', body);

    return loginRes.successful; //Login success
  }

  ///Get the byte data of a bigint
  Uint8List _bigIntToBytes(BigInt data) {

    ByteData bytes = ByteData((data.bitLength/8).ceil());
    BigInt bigDat = data;

    for (var i = 1; i <= bytes.lengthInBytes; i++) {
      bytes.setUint8(bytes.lengthInBytes - i, bigDat.toUnsigned(8).toInt());
      bigDat = bigDat >> 8;
    }

    Uint8List res = bytes.buffer.asUint8List();
    return res;
  }

}