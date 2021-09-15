//This class is for the initial login, and the flow that will come with it.

import 'package:unison/services/Server/judgeService.dart';

import '../../models/global.dart';
import 'backendAPI.dart';

class LoginService {
  //TODO list:
  //Implement login flow when server is ready.
    // - Signing with Metamask etc.

  ApiInteraction _api = ApiInteraction();
  JudgeService _jS = JudgeService(); //TODO: Temporary, will be replaced by a new service soon

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
  Future<void> login() async {
    //TODO: Get nonce, sign, send data, get jwt, set Global.
  }

}